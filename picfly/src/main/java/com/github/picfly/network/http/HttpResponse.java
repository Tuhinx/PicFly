package com.github.picfly.network.http;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents an HTTP response.
 */
public class HttpResponse {
    private final int code;
    private final Map<String, List<String>> headers;
    private final byte[] body;

    /**
     * Creates a new HTTP response.
     *
     * @param code    The response code
     * @param headers The response headers
     * @param body    The response body
     */
    public HttpResponse(int code, Map<String, List<String>> headers, byte[] body) {
        this.code = code;
        this.headers = headers != null ? headers : new HashMap<>();
        this.body = body;
    }

    /**
     * Gets the response code.
     *
     * @return The response code
     */
    public int getCode() {
        return code;
    }

    /**
     * Gets the response headers.
     *
     * @return The response headers
     */
    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    /**
     * Gets the response body as a byte array.
     *
     * @return The response body
     */
    public byte[] getBody() {
        return body;
    }

    /**
     * Gets the response body as an input stream.
     *
     * @return The response body as an input stream
     */
    public InputStream getBodyAsStream() {
        return body != null ? new ByteArrayInputStream(body) : null;
    }

    /**
     * Checks if the response was successful (2xx status code).
     *
     * @return true if the response was successful, false otherwise
     */
    public boolean isSuccessful() {
        return code >= 200 && code < 300;
    }

    /**
     * Gets the content length.
     *
     * @return The content length or -1 if unknown
     */
    public long getContentLength() {
        return body != null ? body.length : -1;
    }
}
