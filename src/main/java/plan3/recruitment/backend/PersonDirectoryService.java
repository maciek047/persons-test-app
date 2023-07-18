package plan3.recruitment.backend;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.jetty.HttpConnectorFactory;
import io.dropwizard.server.DefaultServerFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import plan3.recruitment.backend.resources.PersonResource;

public class PersonDirectoryService extends Application<Configuration> {

    public static void main(final String[] args) throws Exception {
        new PersonDirectoryService().run(args);
    }

    @Override
    public void initialize(final Bootstrap<Configuration> bootstrap) {
    }

    @Override
    public void run(final Configuration configuration, final Environment environment) {
        final PersonResource personResource = new PersonResource();
        environment.jersey().register(personResource);

        HttpConnectorFactory httpFactory = ((DefaultServerFactory) configuration.getServerFactory())
                .getApplicationConnectors()
                .stream()
                .filter(connector -> connector.getClass() == HttpConnectorFactory.class)
                .map(connector -> (HttpConnectorFactory) connector)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Unable to find HTTP connector in the application connectors"));

        httpFactory.setPort(Integer.parseInt(System.getenv("PORT")));
    }
}