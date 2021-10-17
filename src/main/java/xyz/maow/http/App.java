package xyz.maow.http;

import xyz.maow.http.model.HttpResponse;
import xyz.maow.http.net.HttpServer;
import xyz.maow.http.net.HttpServer.Connection;
import xyz.maow.http.log.Logger;
import xyz.maow.http.log.Reporter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class App {
    private static final Reporter REPORTER;
    private static final Logger   LOGGER;

    private static void report(Exception ex)   { REPORTER.reportAndExit(ex);      }
    private static void report(String message) { REPORTER.reportAndExit(message); }

    private static int port(String s) {
        int port = -1;
        try {
            port = Integer.parseInt(s);
        } catch (NumberFormatException ex) {
            report("Exception thrown when trying to parse string as port : " + s + " (" + ex.getMessage() + ")");
        }
        return port;
    }

    public static void main(String[] args) {
        final long start = System.currentTimeMillis();

        if (args.length == 0)
            report("Must specify server port");
        final int port = port(args[0]);

        LOGGER.info("Starting up server...");

        final ExecutorService exec = Executors.newCachedThreadPool();
        try (HttpServer server = new HttpServer(REPORTER, port)) {
            final long end = System.currentTimeMillis();
            LOGGER.info("Server started after " + (end - start) + "ms");
            LOGGER.info("Listening for connections...");
            //noinspection InfiniteLoopStatement
            while (true) {
                final Connection con = server.accept();
                final String address = con.getClient().getRemoteSocketAddress().toString();
                LOGGER.info("Connection accepted from " + address);
                exec.submit(() -> {
                    con.onReceive(request -> {
                        LOGGER.info("Received request from " + address);
                        final HttpResponse response = server.respond(request);
                        if (response != null) {
                            con.send(response);
                            LOGGER.info("Sent a response back to " + address);
                        }
                    });
                    LOGGER.info("Connection disconnected at " + address);
                });
            }
        }
        catch (Exception ex) { report(ex); }
    }

    static {
        REPORTER = new Reporter();
        LOGGER   = Logger.create(REPORTER);
    }
}