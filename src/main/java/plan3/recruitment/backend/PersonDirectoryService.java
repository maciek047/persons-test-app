package plan3.recruitment.backend;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import plan3.recruitment.backend.resources.PersonResource;

public class PersonDirectoryService extends Application<Configuration> {

    @Override
    public void initialize(final Bootstrap<Configuration> bootstrap) {
        bootstrap.setConfigurationSourceProvider(
                new SubstitutingSourceProvider(bootstrap.getConfigurationSourceProvider(),
                        new EnvironmentVariableSubstitutor(false)
                )
        );

    }

    public static void main(final String[] args) throws Exception {
        new PersonDirectoryService().run(args);
    }

    @Override
    public void run(final Configuration configuration, final Environment environment) throws Exception {
        final PersonResource personResource = new PersonResource();
        environment.jersey().register(personResource);
    }
}
