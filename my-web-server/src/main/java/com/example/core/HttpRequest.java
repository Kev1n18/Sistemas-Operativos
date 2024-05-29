package com.example.core;

public class HttpRequest {
    private final String method;
    private final String resource;
    private final String httpVersion;

    public HttpRequest(String method, String resource, String httpVersion) {
        this.method = method;
        this.resource = resource;
        this.httpVersion = httpVersion;
    }

    public String getMethod() {
        return method;
    }

    public String getResource() {
        return resource;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    @Override
    public String toString() {
        return "HttpRequest{" +
                "method='" + method + '\'' +
                ", resource='" + resource + '\'' +
                ", httpVersion='" + httpVersion + '\'' +
                '}';
    }
}
