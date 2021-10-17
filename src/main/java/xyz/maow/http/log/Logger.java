package xyz.maow.http.log;

import org.fusesource.jansi.AnsiConsole;
import org.fusesource.jansi.AnsiPrintStream;

import java.time.LocalDateTime;

import static org.fusesource.jansi.Ansi.ansi;
import static xyz.maow.http.log.Reporter.Listener;
import static xyz.maow.http.log.Reporter.Error;

public final class Logger {
    private static final AnsiPrintStream out = AnsiConsole.out();

    private Logger() {}

    public static Logger create() {
        return new Logger();
    }

    public static Logger create(Reporter reporter) {
        final Logger log = create();
        reporter.listen(new ErrorLogger(log));
        return log;
    }

    private String time() {
        final LocalDateTime time = LocalDateTime.now();
        return time.getHour()
            + ":" + time.getMinute()
            + ":" + time.getSecond();
    }

    public void info(Object o) {
        final String message = time() + " [INFO] " + o.toString();
        out.println(ansi(message.length())
            .fgBrightBlue()
            .a(message)
            .reset());
    }

    public void warn(Object o) {
        final String message = time() + " [WARN] " + o.toString();
        out.println(ansi(message.length())
            .fgBrightYellow()
            .a(message)
            .reset());
    }

    public void err(Object o) {
        final String message = time() + " [ERR] " + o.toString();
        out.println(ansi(message.length())
            .fgBrightRed()
            .a(message)
            .reset());
    }

    private static class ErrorLogger implements Listener {
        private final Logger logger;

        private ErrorLogger(Logger logger) {
            this.logger = logger;
        }

        @Override
        public void update(Error err) {
            logger.err(err);
        }
    }
}