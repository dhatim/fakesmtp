package org.dhatim.fakesmtp.server;

import com.dumbster.smtp.MailMessage;
import com.dumbster.smtp.SmtpServer;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;
import javax.mail.MessagingException;
import javax.mail.internet.MimeUtility;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.dhatim.fakesmtp.client.Mail;

@Path("/mails")
@Produces(MediaType.APPLICATION_JSON)
public class MailResource {

    private final DumbsterManager manager;

    public MailResource(DumbsterManager manager) {
        this.manager = manager;
    }

    @GET
    public List<Mail> list() {
        return Arrays.stream(getServer().getMessages()).map(MailResource::fromMailMessage).collect(Collectors.toList());
    }

    @GET
    @Path("count")
    public int count() {
        return getServer().getEmailCount();
    }

    @POST
    @Path("clear")
    public int clear() {
        int count = getServer().getEmailCount();
        getServer().clearMessages();
        return count;
    }

    private SmtpServer getServer() {
        return manager.getServer();
    }

    private static Mail fromMailMessage(MailMessage mail) {
        Iterable<String> it = () -> mail.getHeaderNames();
        LinkedHashMap<String, List<String>> hs = new LinkedHashMap<>();
        for (String name : it) {
            hs.put(name, Arrays.asList(mail.getHeaderValues(name)));
        }
        return new Mail(hs, mail.getBody(), decodeOrEmpty(mail));
    }

    private static String decodeOrEmpty(MailMessage mail) {
        try {
            return decode(mail);
        } catch (IOException e) {
            return null;
        }
    }

    private static String decode(MailMessage mail) throws IOException {
        String encoding = mail.getFirstHeaderValue("Content-Transfer-Encoding");
        String result;
        if (encoding == null) {
            result = null;
        } else {
            try (InputStream is = MimeUtility.decode(new ByteArrayInputStream(mail.getBody().getBytes(StandardCharsets.US_ASCII)), encoding)) {
                result = toString(is, StandardCharsets.UTF_8);
            } catch (MessagingException e) {
                throw new IOException(e);
            }
        }
        return result;
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
