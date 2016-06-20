package org.dhatim.fakesmtp.server;

import com.dumbster.smtp.ServerOptions;
import com.dumbster.smtp.SmtpServer;
import com.dumbster.smtp.SmtpServerFactory;
import io.dropwizard.lifecycle.Managed;

public class DumbsterManager implements Managed {

    private final ServerOptions options;
    private SmtpServer server;
    
    public DumbsterManager(FakeSmtpConfiguration configuration) {
        options = new ServerOptions();
        options.port = configuration.getSmtpPort();
    }
    
    @Override
    public void start() throws Exception {
        server = SmtpServerFactory.startServer(options);
    }

    @Override
    public void stop() throws Exception {
        if (server != null) {
            server.stop();
        }
    }
    
    public SmtpServer getServer() {
        return server;
    }

}
