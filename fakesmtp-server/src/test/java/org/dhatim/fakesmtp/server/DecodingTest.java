package org.dhatim.fakesmtp.server;

import static org.assertj.core.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.junit.Test;

public class DecodingTest {

    private static final String ENCODED = "Hi,\nYou have been invited to dsn.\nClick here to accept the invitation:\nhttps://change.me.example.com?cpToken=3De2369c1e-c1ba-4607-a3bb-ccf83c7ee49=\n8";
    private static final String DECODED = "Hi,\nYou have been invited to dsn.\nClick here to accept the invitation:\nhttps://change.me.example.com?cpToken=e2369c1e-c1ba-4607-a3bb-ccf83c7ee498";

    @Test
    public void testDecode() throws IOException {
        assertThat(Decoding.decode("quoted-printable", toInputStream(ENCODED))).isEqualTo(DECODED);
    }

    @Test(expected=IOException.class)
    public void testDecodeWithInvalidEncoding() throws IOException {
        Decoding.decode("no-encoding", toInputStream(ENCODED));
    }

    private static InputStream toInputStream(String s) {
        return new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
    }

}
