package com.github.picfly.network.http;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents an HTTP request.
 */
public class HttpRequest {
    private final String url;
    private final String method;
    private final Map<String, String> headers;
    private byte[] body;
    private int connectTimeout = 15000; // 15 seconds default
    private int readTimeout = 20000; // 20 seconds default

    /**
     * Creates a new GET request.
     *
     * @param url The URL to request
     * @return A new HttpRequest
     */
    public static HttpRequest get(String url) {
        return new HttpRequest(url, "GET");
    }

    /**
     * Creates a new POST request.
     *
     * @param url The URL to request
     * @return A new HttpRequest
     */
    public static HttpRequest post(String url) {
        return new HttpRequest(url, "POST");
    }

    /**
     * Creates a new request with the specified method.
     *
     * @param url    The URL to request
     * @param method The HTTP method
     */
    public HttpRequest(String url, String method) {
        this.url = url;
        this.method = method;
        this.headers = new HashMap<>();
    }

    /**
     * Adds a header to the request.
     *
     * @param name  The header name
     * @param value The header value
     * @return This request for chaining
     */
    public HttpRequest addHeader(String name, String value) {
        headers.put(name, value);
        return this;
    }

    /**
     * Sets the request body.
     *
     * @param body The request body
     * @return This request for chaining
     */
    public HttpRequest setBody(byte[] body) {
        this.body = body;
        return this;
    }

    /**
     * Sets the connect timeout.
     *
     * @param timeoutMs The timeout in milliseconds
     * @return This request for chaining
     */
    public HttpRequest setConnectTimeout(int timeoutMs) {
        this.connectTimeout = timeoutMs;
        return this;
    }

    /**
     * Sets the read timeout.
     *
     * @param timeoutMs The timeout in milliseconds
     * @return This request for chaining
     */
    public HttpRequest setReadTimeout(int timeoutMs) {
        this.readTimeout = timeoutMs;
        return this;
    }

    /**
     * Gets the URL.
     *
     * @return The URL
     */
    public String getUrl() {
        return url;
    }

    /**
     * Gets the HTTP method.
     *
     * @return The HTTP method
     */
    public String getMethod() {
        return method;
    }

    /**
     * Gets the headers.
     *
     * @return The headers
     */
    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * Gets the request body.
     *
     * @return The request body
     */
    public byte[] getBody() {
        return body;
    }

    /**
     * Gets the connect timeout.
     *
     * @return The connect timeout in milliseconds
     */
    public int getConnectTimeout() {
        return connectTimeout;
    }

    /**
     * Gets the read timeout.
     *
     * @return The read timeout in milliseconds
     */
    public int getReadTimeout() {
        return readTimeout;
    }
}
