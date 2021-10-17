package xyz.maow.http.net;

import xyz.maow.http.log.Reporter;

import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public final class IOUtil {
    private IOUtil() {
        throw new UnsupportedOperationException("I'M THE TRASH MAN, I EAT GARBAGE.");
    }

    public static BufferedReader toReader(InputStream stream) {
        return new BufferedReader(new InputStreamReader(stream));
    }

    public static void writeChars(OutputStream stream, String s) throws IOException {
        stream.write(s.getBytes(StandardCharsets.UTF_8));
    }

    public static byte[] getResource(Reporter reporter, URI uri) {
        try (InputStream in = IOUtil.class.getResourceAsStream(uri.toString());
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            if (in == null) return null;
            final byte[] buffer = new byte[4096];
            int len;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            return out.toByteArray();
        } catch (IOException ex) { reporter.report(ex); }
        return null;
    }
}