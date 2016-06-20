package org.dhatim.fakesmtp.client;

import java.util.HashMap;
import java.util.List;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import org.glassfish.jersey.jackson.JacksonFeature;

public class FakeSmtpClient implements AutoCloseable {
    
    private static final String URL = "http://%s:%d/%s";

    private final Client client;

    private final String hostname;
    private final int port;
    
    public FakeSmtpClient(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
        client = ClientBuilder.newBuilder()
                .register(JacksonFeature.class)
                .build();
    }
    
    public List<Mail> getMails() {
        WebTarget target = client.target(compose("mails"));
        return target.request(MediaType.APPLICATION_JSON_TYPE)
                .get(new GenericType<List<Mail>>() {});
    }
    
    public int getMailCount() {
        WebTarget target = client.target(compose("mails/count"));
        return target.request(MediaType.APPLICATION_JSON_TYPE)
                .get(Integer.class);
    }
    
    public int clear() {
        WebTarget target = client.target(compose("mails/clear"));
        return target.request(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(new HashMap<>()), Integer.class);
    }

    @Override
    public void close() {
        client.close();
    }
    
    private String compose(String urlSuffix) {
        return String.format(URL, hostname, port, urlSuffix);
    }
    
}
