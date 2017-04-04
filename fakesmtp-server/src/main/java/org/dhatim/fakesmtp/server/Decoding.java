package org.dhatim.fakesmtp.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import javax.mail.MessagingException;
import javax.mail.internet.MimeUtility;

public final class Decoding {

    private Decoding() {
    }

    public static String decode(String contentTransferEncoding, InputStream inputStream) throws IOException {
        try (InputStream is = MimeUtility.decode(inputStream, contentTransferEncoding)) {
            return toString(is, StandardCharsets.UTF_8);
        } catch (MessagingException e) {
            throw new IOException(e);
        }
    }

    private static String toString(InputStream inputStream, Charset charset) throws IOException {
        StringBuilder builder = new StringBuilder();
        try (Reader reader = new BufferedReader(new InputStreamReader(inputStream, charset))) {
            int c = 0;
            while ((c = reader.read()) != -1) {
                builder.append((char) c);
            }
        }
        return builder.toString();
    }

}
