package org.dhatim.fakesmtp.server.it;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.Date;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.dhatim.fakesmtp.client.FakeSmtpClient;
import org.dhatim.fakesmtp.client.Mail;
import org.junit.Test;

public class SimpleServer {
    
    private static final int SMTP_SERVER_PORT = Integer.parseInt(System.getenv("FAKESMTP_SMTP_PORT"));
    private static final int HTTP_SERVER_PORT = Integer.parseInt(System.getenv("FAKESMTP_HTTP_PORT"));

    @Test
    public void testSendOneMail() throws AddressException, MessagingException {
        try (FakeSmtpClient client = new FakeSmtpClient("localhost", HTTP_SERVER_PORT)) {
            assertThat(client.getMailCount()).isZero();
            
            Properties props = new Properties();
            props.put("mail.smtp.host", "localhost");
            props.put("mail.smtp.port", SMTP_SERVER_PORT);
            
            Session session = Session.getDefaultInstance(props);
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("me@thisisme.org"));
            InternetAddress[] internetAddresses = new InternetAddress[1];
            internetAddresses[0] = new InternetAddress("other@thisisme.org");
            message.setRecipients(Message.RecipientType.TO, internetAddresses);
            message.setSubject("Test");
            message.setText("test mail");
            message.setHeader("X-Mailer", "Java");
            message.setSentDate(new Date());
            
            Transport.send(message);
            
            assertThat(client.getMailCount()).isNotZero();
            Mail mail = client.getMails().get(0);
            
            assertThat(mail.getBody()).isEqualTo("test mail");
            assertThat(mail.getHeaders()).containsEntry("Subject", Arrays.asList("Test"));
        }
    }
    
}
