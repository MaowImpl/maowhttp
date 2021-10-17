package xyz.maow.http.net;

import xyz.maow.http.model.HttpHeader;
import xyz.maow.http.model.HttpResponse;
import xyz.maow.http.model.HttpVersion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class ResponseBuilder {
    private HttpVersion version;
    private int statusCode;
    private String statusText;
    private List<HttpHeader> headers;
    private StringBuilder body;

    public ResponseBuilder() {
        this.version = HttpVersion.V1_1;
        this.headers = new ArrayList<>();
        this.body    = new StringBuilder();
    }

    public ResponseBuilder setVersion(HttpVersion version) {
        this.version = version;
        return this;
    }

    public ResponseBuilder setStatusCode(int code) {
        this.statusCode = code;
        return this;
    }

    public ResponseBuilder setStatusText(String text) {
        this.statusText = text;
        return this;
    }

    public ResponseBuilder setStatus(int code, String text) {
        this.statusCode = code;
        this.statusText = text;
        return this;
    }

    public ResponseBuilder addHeader(String name, String value) {
        this.headers.add(new HttpHeader(name, value));
        return this;
    }

    public ResponseBuilder addHeaders(HttpHeader... headers) {
        this.headers.addAll(Arrays.asList(headers));
        return this;
    }

    public ResponseBuilder setHeaders(HttpHeader... headers) {
        this.headers = new ArrayList<>(Arrays.asList(headers));
        return this;
    }

    public ResponseBuilder setBody(String body) {
        this.body = new StringBuilder(body);
        return this;
    }

    public ResponseBuilder appendBody(String afterBody) {
        this.body.append(afterBody).append('\n');
        return this;
    }

    public HttpResponse build() {
        return new HttpResponse(version, statusCode, statusText, headers, body.toString());
    }
}