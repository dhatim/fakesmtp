package org.dhatim.fakesmtp.server;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.dumbster.smtp.MailMessage;
import com.dumbster.smtp.MailMessageImpl;
import com.dumbster.smtp.SmtpServer;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.dropwizard.testing.junit.ResourceTestRule;
import java.util.Arrays;
import java.util.List;
import javax.ws.rs.core.GenericType;
import org.dhatim.fakesmtp.client.Mail;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

public class MailResourceTest {

    private static final SmtpServer serverDao = mock(SmtpServer.class);
    private static final DumbsterManager dao = mock(DumbsterManager.class);

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new MailResource(dao))
            .build();

    private final Mail mail = new Mail(ImmutableMap.of(
            "header1", ImmutableList.of("value1"),
            "header2", ImmutableList.of("value1", "value2"),
            "Content-Transfer-Encoding", ImmutableList.of("quoted-printable")),
            "This is a test", "This is a test");

    private final Mail mail2 = new Mail(
            ImmutableMap.of(
                    "From", ImmutableList.of("Change Me <change-me@example.com>"),
                    "To", ImmutableList.of("ituser_invited_dhrosl75@dhatim.com"),
                    "Content-Transfer-Encoding", ImmutableList.of("quoted-printable"),
                    "Subject", ImmutableList.of("Invitation"),
                    "Content-Type", ImmutableList.of("text/plain")
            ),
            "Hi,\nYou have been invited to dsn.\nClick here to accept the invitation:\nhttps://change.me.example.com?cpToken=3De2369c1e-c1ba-4607-a3bb-ccf83c7ee49=\n8",
            "Hi,\nYou have been invited to dsn.\nClick here to accept the invitation:\nhttps://change.me.example.com?cpToken=e2369c1e-c1ba-4607-a3bb-ccf83c7ee498");

    @Before
    public void setUp() {
        MailMessageImpl message = new MailMessageImpl();
        message.addHeader("header1", "value1");
        message.addHeader("header2", "value1");
        message.addHeader("header2", "value2");
        message.addHeader("Content-Transfer-Encoding", "quoted-printable");
        message.appendBody("This is a test");

        MailMessageImpl message2 = new MailMessageImpl();
        message2.addHeader("From", "Change Me <change-me@example.com>");
        message2.addHeader("To", "ituser_invited_dhrosl75@dhatim.com");
        message2.addHeader("Content-Transfer-Encoding", "quoted-printable");
        message2.addHeader("Subject", "Invitation");
        message2.addHeader("Content-Type", "text/plain");
        message2.appendBody("Hi,");
        message2.appendBody("You have been invited to dsn.");
        message2.appendBody("Click here to accept the invitation:");
        message2.appendBody("https://change.me.example.com?cpToken=3De2369c1e-c1ba-4607-a3bb-ccf83c7ee49=");
        message2.appendBody("8");

        when(dao.getServer()).thenReturn(serverDao);
        when(serverDao.getMessages()).thenReturn(new MailMessage[] {message, message2});
    }

    @After
    public void tearDown() {
        reset(dao);
        reset(serverDao);
    }

    @Test
    public void testGetMessages() {
        assertThat(resources.client().target("/mails").request().get(new GenericType<List<Mail>>() {}))
                .isEqualTo(Arrays.asList(mail, mail2));
        verify(dao).getServer();
    }

}
