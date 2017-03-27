package org.dhatim.fakesmtp.server;

import static io.dropwizard.testing.FixtureHelpers.*;
import static org.assertj.core.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.dropwizard.jackson.Jackson;
import java.io.IOException;
import org.dhatim.fakesmtp.client.Mail;
import org.junit.Test;

public class MailTest {

    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

    @Test
    public void serialize() throws IOException {
        final Mail mail = new Mail(ImmutableMap.of("header1", ImmutableList.of("value1"), "header2", ImmutableList.of("value1", "value2")), "This is a test", "This is a test");

        final String expected = MAPPER.writeValueAsString(MAPPER.readValue(fixture("fixtures/mail.json"), Mail.class));

        assertThat(MAPPER.writeValueAsString(mail)).isEqualTo(expected);
    }

    @Test
    public void deserialize() throws IOException {
        final Mail mail = new Mail(ImmutableMap.of("header1", ImmutableList.of("value1"), "header2", ImmutableList.of("value1", "value2")), "This is a test", "This is a test");
        assertThat(MAPPER.readValue(fixture("fixtures/mail.json"), Mail.class))
                .isEqualTo(mail);
    }

}
