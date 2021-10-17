package xyz.maow.http.log;

import java.util.HashSet;
import java.util.Set;

public final class Reporter {
    private final Set<Listener> listeners;

    public Reporter() {
        this.listeners = new HashSet<>();
    }

    public void report(String message) {
        updateAll(new Error(message));
    }

    public void report(Exception ex) {
        updateAll(new Error(ex.getMessage()));
    }

    public void reportAndExit(String message) {
        report(message);
        System.exit(-1);
    }

    public void reportAndExit(Exception ex) {
        report(ex);
        System.exit(-1);
    }

    public void listen(Listener listener) {
        listeners.add(listener);
    }

    private void updateAll(Error error) {
        for (Listener listener : listeners)
            listener.update(error);
    }

    public static final class Error {
        private final String message;

        private Error(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        @Override
        public String toString() {
            return message;
        }
    }

    public interface Listener {
        void update(Error err);
    }
}