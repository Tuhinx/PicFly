package com.github.picfly.network.http;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A simple HTTP client implementation using HttpURLConnection.
 */
public class HttpClient {
    private static final String TAG = "PicFly-HttpClient";
    private final Map<String, HttpURLConnection> activeConnections = new ConcurrentHashMap<>();

    /**
     * Interface for HTTP response callbacks.
     */
    public interface Callback {
        void onResponse(HttpResponse response);
        void onFailure(IOException e);
    }

    /**
     * Executes an HTTP request synchronously.
     *
     * @param request The request to execute
     * @return The response
     * @throws IOException If an I/O error occurs
     */
    public HttpResponse execute(HttpRequest request) throws IOException {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(request.getUrl());
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(request.getMethod());
            connection.setConnectTimeout(request.getConnectTimeout());
            connection.setReadTimeout(request.getReadTimeout());
            
            // Add headers
            for (Map.Entry<String, String> header : request.getHeaders().entrySet()) {
                connection.setRequestProperty(header.getKey(), header.getValue());
            }
            
            // Set body if present
            if (request.getBody() != null) {
                connection.setDoOutput(true);
                connection.getOutputStream().write(request.getBody());
            }
            
            // Get response
            int responseCode = connection.getResponseCode();
            
            // Read headers
            Map<String, List<String>> headers = new HashMap<>();
            for (Map.Entry<String, List<String>> entry : connection.getHeaderFields().entrySet()) {
                if (entry.getKey() != null) {
                    headers.put(entry.getKey(), new ArrayList<>(entry.getValue()));
                }
            }
            
            // Read body
            InputStream inputStream;
            if (responseCode >= 400) {
                inputStream = connection.getErrorStream();
            } else {
                inputStream = connection.getInputStream();
            }
            
            byte[] body = null;
            if (inputStream != null) {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                body = outputStream.toByteArray();
                inputStream.close();
            }
            
            return new HttpResponse(responseCode, headers, body);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * Executes an HTTP request asynchronously.
     *
     * @param request  The request to execute
     * @param callback The callback to notify of success or failure
     */
    public void enqueue(final HttpRequest request, final Callback callback) {
        Thread thread = new Thread(() -> {
            HttpURLConnection connection = null;
            try {
                URL url = new URL(request.getUrl());
                connection = (HttpURLConnection) url.openConnection();
                
                // Store the connection for potential cancellation
                activeConnections.put(request.getUrl(), connection);
                
                connection.setRequestMethod(request.getMethod());
                connection.setConnectTimeout(request.getConnectTimeout());
                connection.setReadTimeout(request.getReadTimeout());
                
                // Add headers
                for (Map.Entry<String, String> header : request.getHeaders().entrySet()) {
                    connection.setRequestProperty(header.getKey(), header.getValue());
                }
                
                // Set body if present
                if (request.getBody() != null) {
                    connection.setDoOutput(true);
                    connection.getOutputStream().write(request.getBody());
                }
                
                // Get response
                int responseCode = connection.getResponseCode();
                
                // Read headers
                Map<String, List<String>> headers = new HashMap<>();
                for (Map.Entry<String, List<String>> entry : connection.getHeaderFields().entrySet()) {
                    if (entry.getKey() != null) {
                        headers.put(entry.getKey(), new ArrayList<>(entry.getValue()));
                    }
                }
                
                // Read body
                InputStream inputStream;
                if (responseCode >= 400) {
                    inputStream = connection.getErrorStream();
                } else {
                    inputStream = connection.getInputStream();
                }
                
                byte[] body = null;
                if (inputStream != null) {
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                    body = outputStream.toByteArray();
                    inputStream.close();
                }
                
                final HttpResponse response = new HttpResponse(responseCode, headers, body);
                
                if (callback != null) {
                    callback.onResponse(response);
                }
            } catch (IOException e) {
                Log.e(TAG, "Error executing request: " + e.getMessage());
                if (callback != null) {
                    callback.onFailure(e);
                }
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                activeConnections.remove(request.getUrl());
            }
        });
        
        thread.start();
    }

    /**
     * Cancels all active requests.
     */
    public void cancelAll() {
        for (HttpURLConnection connection : activeConnections.values()) {
            connection.disconnect();
        }
        activeConnections.clear();
    }
}
