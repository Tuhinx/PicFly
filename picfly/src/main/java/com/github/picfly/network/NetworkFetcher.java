package com.github.picfly.network;

import android.graphics.Bitmap;
import android.util.Log;

import com.github.picfly.util.BitmapUtils;
import com.github.picfly.util.ThreadUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Handles network operations for fetching images.
 */
public class NetworkFetcher {
    private static final String TAG = "PicFly-NetworkFetcher";
    private static final int DEFAULT_CONNECT_TIMEOUT_SECONDS = 15;
    private static final int DEFAULT_READ_TIMEOUT_SECONDS = 20;

    private final OkHttpClient okHttpClient;

    /**
     * Creates a new NetworkFetcher with default timeouts.
     */
    public NetworkFetcher() {
        this(DEFAULT_CONNECT_TIMEOUT_SECONDS, DEFAULT_READ_TIMEOUT_SECONDS);
    }

    /**
     * Creates a new NetworkFetcher with custom timeouts.
     *
     * @param connectTimeoutSeconds Connection timeout in seconds
     * @param readTimeoutSeconds    Read timeout in seconds
     */
    public NetworkFetcher(int connectTimeoutSeconds, int readTimeoutSeconds) {
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(connectTimeoutSeconds, TimeUnit.SECONDS)
                .readTimeout(readTimeoutSeconds, TimeUnit.SECONDS)
                .build();
    }

    /**
     * Interface for network fetch callbacks.
     */
    public interface FetchCallback {
        void onSuccess(Bitmap bitmap);

        void onFailure(Exception e);
    }

    /**
     * Fetches an image from a URL.
     *
     * @param url      The URL to fetch the image from
     * @param callback The callback to notify of success or failure
     */
    public void fetchImage(String url, final FetchCallback callback) {
        if (url == null || url.isEmpty()) {
            if (callback != null) {
                callback.onFailure(new IllegalArgumentException("URL cannot be null or empty"));
            }
            return;
        }

        Log.d(TAG, "Starting image fetch from URL: " + url);

        Request request = new Request.Builder()
                .url(url)
                .build();

        Log.d(TAG, "Enqueuing request for URL: " + url);
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Failed to fetch image: " + e.getMessage());
                if (callback != null) {
                    ThreadUtils.executeOnMainThread(() -> callback.onFailure(e));
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, "Received response for URL: " + call.request().url() + ", code: " + response.code());

                if (!response.isSuccessful()) {
                    String errorMsg = "Unexpected response code: " + response.code();
                    Log.e(TAG, errorMsg);
                    if (callback != null) {
                        ThreadUtils.executeOnMainThread(() -> callback
                                .onFailure(new IOException(errorMsg)));
                    }
                    return;
                }

                ResponseBody responseBody = response.body();
                if (responseBody == null) {
                    String errorMsg = "Empty response body";
                    Log.e(TAG, errorMsg);
                    if (callback != null) {
                        ThreadUtils
                                .executeOnMainThread(() -> callback.onFailure(new IOException(errorMsg)));
                    }
                    return;
                }

                Log.d(TAG, "Response body received, content length: " +
                        (responseBody.contentLength() != -1 ? responseBody.contentLength() : "unknown"));

                try (InputStream inputStream = responseBody.byteStream()) {
                    Log.d(TAG, "Starting bitmap decoding from stream");
                    final Bitmap bitmap = BitmapUtils.decodeStream(inputStream);

                    if (bitmap == null) {
                        String errorMsg = "Failed to decode bitmap";
                        Log.e(TAG, errorMsg);
                        if (callback != null) {
                            ThreadUtils.executeOnMainThread(
                                    () -> callback.onFailure(new IOException(errorMsg)));
                        }
                        return;
                    }

                    Log.d(TAG, "Bitmap decoded successfully, size: " + bitmap.getWidth() + "x" + bitmap.getHeight());

                    if (callback != null) {
                        ThreadUtils.executeOnMainThread(() -> {
                            Log.d(TAG, "Delivering bitmap to callback on main thread");
                            callback.onSuccess(bitmap);
                        });
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error processing response", e);
                    if (callback != null) {
                        ThreadUtils.executeOnMainThread(() -> callback.onFailure(e));
                    }
                }
            }
        });
    }

    /**
     * Cancels all ongoing requests.
     */
    public void cancelAll() {
        okHttpClient.dispatcher().cancelAll();
    }
}
