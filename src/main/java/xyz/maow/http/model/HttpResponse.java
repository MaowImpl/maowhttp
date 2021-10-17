package xyz.maow.http.model;

import xyz.maow.http.net.ResponseBuilder;

import java.util.List;

public final class HttpResponse {
    private final HttpVersion version;
    private final int statusCode;
    private final String statusText;
    private final List<HttpHeader> headers;
    private final String body;

    public HttpResponse(HttpVersion version,
                        int statusCode,
                        String statusText,
                        List<HttpHeader> headers,
                        String body) {
        this.version = version;
        this.statusCode = statusCode;
        this.statusText = statusText;
        this.headers = headers;
        this.body = body;
    }

    public static ResponseBuilder builder() {
        return new ResponseBuilder();
    }

    public HttpVersion getVersion() {
        return version;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getStatusText() {
        return statusText;
    }

    public List<HttpHeader> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder
            .append(version)
            .append(' ').append(statusCode)
            .append(' ').append(statusText)
            .append('\n');
        for (HttpHeader header : headers) {
            builder
                .append(header.getName())
                .append(": ")
                .append(header.getValue())
                .append('\n');
        }
        builder
            .append('\n')
            .append(body);
        return builder.toString();
    }
}