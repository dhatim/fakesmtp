package org.dhatim.fakesmtp.server;

import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class FakeSmtpApplication extends Application<FakeSmtpConfiguration> {

    @Override
    public void initialize(Bootstrap<FakeSmtpConfiguration> bootstrap) {
        bootstrap.setConfigurationSourceProvider(new SubstitutingSourceProvider(bootstrap.getConfigurationSourceProvider(), new EnvironmentVariableSubstitutor(false)));
    }
    
    @Override
    public void run(FakeSmtpConfiguration configuration, Environment environment) throws Exception {
        DumbsterManager dumbster = new DumbsterManager(configuration);
        environment.lifecycle().manage(dumbster);
        environment.jersey().register(new MailResource(dumbster));
        environment.healthChecks().register("dumbster", new DumbsterHealthCheck(dumbster));
    }
    
    @Override
    public String getName() {
        return "fakesmtp";
    }
    
    public static void main(String[] args) throws Exception {
        new FakeSmtpApplication().run(args);
    }

}
