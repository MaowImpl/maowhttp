package xyz.maow.http.net;

import xyz.maow.http.model.HttpRequest;
import xyz.maow.http.model.HttpResponse;
import xyz.maow.http.model.HttpVersion;
import xyz.maow.http.log.Reporter;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.function.Consumer;

public final class HttpServer implements Closeable {
    private final Reporter reporter;
    private final ServerSocket server;

    public HttpServer(Reporter reporter, int port) throws IOException {
        this.reporter = reporter;
        this.server = new ServerSocket(port);
    }

    public Connection accept() throws IOException {
        final Socket client = server.accept();
        client.setKeepAlive(true);
        return new Connection(client);
    }

    public HttpResponse respond(HttpRequest request) {
        if (request.getVersion() != HttpVersion.V1_1) {
            reporter.report("Request was a higher version than supported " + request.getVersion());
            return null;
        }
        try {
            switch (request.getMethod()) {
                case GET: {
                    final byte[] content = IOUtil.getResource(reporter, new URI("/res" + request.getTarget()));
                    if (content == null) break;
                    return HttpResponse.builder()
                        .setStatus(200, "OK")
                        .addHeader("Content-Length", String.valueOf(content.length))
                        .addHeader("Content-Type", "text/html; charset=UTF-8") // concerning, don't care
                        .addHeader("Server", "maowhttp")
                        .setBody(new String(content))
                        .build();
                }
                case HEAD:
                case POST:
                case PUT:
                case DELETE:
                case CONNECT:
                case OPTIONS:
                case TRACE:
                case PATCH:
                    reporter.report("maowhttp has no support for the " + request.getMethod() + " method currently");
                    break;
            }
        } catch (URISyntaxException ex) { reporter.report(ex); }
        return null;
    }

    @Override
    public void close() throws IOException {
        server.close();
    }

    public class Connection implements Closeable {
        private final Socket client;
        private final BufferedReader reader;
        private final OutputStream out;

        public Connection(Socket client) throws IOException {
            this.client = client;
            this.reader = IOUtil.toReader(client.getInputStream());
            this.out    = client.getOutputStream();
        }

        public void onReceive(Consumer<HttpRequest> consumer) {
            while (true) {
                final String text = receive();
                if (text == null) {
                    closeCaught();
                    break;
                }
                final RequestParser parser = new RequestParser(reporter, text);
                final HttpRequest request = parser.parse();
                if (request != null)
                    consumer.accept(request);
            }
        }

        public String receive() {
            try {
                final StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null && line.length() != 0) {
                    builder.append(line).append('\n');
                }
                return (line == null)
                    ? null
                    : builder.toString();
            }
            catch (IOException ex) {
                reporter.report(ex);
                return null;
            }
        }

        public void send(HttpResponse response) {
            try {
                final String text = response.toString();
                IOUtil.writeChars(out, text);
            } catch (IOException ex) { reporter.report(ex); }
        }

        public Socket getClient() {
            return client;
        }

        private void closeCaught() {
            try {
                close();
            } catch (IOException ex) {
                reporter.report(ex);
            }
        }

        @Override
        public void close() throws IOException {
            client.close();
        }
    }
}