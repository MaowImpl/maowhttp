package xyz.maow.http.net;

import xyz.maow.http.model.HttpHeader;
import xyz.maow.http.model.HttpMethod;
import xyz.maow.http.model.HttpRequest;
import xyz.maow.http.model.HttpVersion;
import xyz.maow.http.log.Reporter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class RequestParser {
    private final Reporter reporter;
    private final String[] contents;

    public RequestParser(Reporter reporter, String contents) {
        this.reporter = reporter;
        this.contents = contents.split("\n");
    }

    public HttpRequest parse() {
        final int length = contents.length;
        if (length < 1) {
            reporter.report("HTTP request must contain one or more lines");
            return null;
        }
        try {
            int index = 0;
            final String[] start = contents[index++].split("\\s+");
            if (start.length != 3) {
                reporter.report("HTTP request start line must contain 3 values : <METHOD> <TARGET> <VERSION>");
                return null;
            }
            final HttpMethod method = HttpMethod.valueOf(start[0]);
            final String target = start[1];
            final HttpVersion version = HttpVersion.valueOf("V" + start[2].substring(5).replace('.', '_'));

            String line;
            final List<HttpHeader> headers = new ArrayList<>();
            while (index < length && !Objects.equals(line = contents[index++], "")) {
                final int separator = line.indexOf(':');
                final String name  = line.substring(0, separator);
                final String value = line.substring(separator + 1).trim();
                headers.add(new HttpHeader(name, value));
            }

            final StringBuilder body = new StringBuilder();
            while (index < length) {
                body
                    .append(contents[index])
                    .append('\n');
                index++;
            }

            return new HttpRequest(method, target, version, headers, body.toString());
        } catch (Exception ex) {
            reporter.report(ex);
            return null;
        }
    }
}