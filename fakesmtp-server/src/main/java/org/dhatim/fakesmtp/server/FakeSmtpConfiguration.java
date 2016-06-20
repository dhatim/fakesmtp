package org.dhatim.fakesmtp.server;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.validation.PortRange;

public class FakeSmtpConfiguration extends Configuration {
    
    @PortRange
    private int smtpPort = 2525;
    
    @JsonProperty
    public int getSmtpPort() {
        return smtpPort;
    }
    
    @JsonProperty
    public void setSmtpPort(int smtpPort) {
        this.smtpPort = smtpPort;
    }
    
}
