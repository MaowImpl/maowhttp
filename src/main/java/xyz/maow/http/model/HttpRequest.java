package xyz.maow.http.model;

import java.util.List;
import java.util.Objects;

public final class HttpRequest {
    private final HttpMethod method;
    private final String target;
    private final HttpVersion version;
    private final List<HttpHeader> headers;
    private final String body;

    public HttpRequest(HttpMethod method,
                       String target,
                       HttpVersion version,
                       List<HttpHeader> headers,
                       String body) {
        this.method = method;
        this.target = target;
        this.version = version;
        this.headers = headers;
        this.body = body;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getTarget() {
        return target;
    }

    public HttpVersion getVersion() {
        return version;
    }

    public List<HttpHeader> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HttpRequest that = (HttpRequest) o;
        return method == that.method && Objects.equals(target, that.target) && version == that.version && Objects.equals(headers, that.headers) && Objects.equals(body, that.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(method, target, version, headers, body);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder
            .append(method).append(' ')
            .append(target).append(' ')
            .append(version)
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