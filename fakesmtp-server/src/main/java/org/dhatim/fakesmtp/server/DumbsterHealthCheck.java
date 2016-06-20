package org.dhatim.fakesmtp.server;

import com.codahale.metrics.health.HealthCheck;
import com.dumbster.smtp.SmtpServer;

public class DumbsterHealthCheck extends HealthCheck {

    private final DumbsterManager dumbster;

    public DumbsterHealthCheck(DumbsterManager dumbster) {
        this.dumbster = dumbster;
    }
    
    @Override
    protected Result check() throws Exception {
        SmtpServer server = dumbster.getServer();
        if (server == null) {
            return Result.unhealthy("smtp server is null");
        }
        if (server.isStopped()) {
            return Result.unhealthy("smtp server is stopped");
        }
        if (!server.isReady()) {
            return Result.unhealthy("smtp server is not ready");
        }
        return Result.healthy();
    }

}
