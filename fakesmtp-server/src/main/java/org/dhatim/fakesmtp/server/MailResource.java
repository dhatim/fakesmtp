package org.dhatim.fakesmtp.server;

import com.dumbster.smtp.MailMessage;
import com.dumbster.smtp.SmtpServer;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;
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
        return new Mail(hs, mail.getBody());
    }
    
}
