package plan3.recruitment.backend.resources;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import plan3.recruitment.backend.model.Person;

import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(DropwizardExtensionsSupport.class)
public class PersonsResourceTest {

    private static final ResourceExtension RESOURCE_EXTENSION = ResourceExtension.builder()
            .addResource(new PersonResource())
            .build();

    @Test
    public void listSortedOnLastname() {
        final Person stefan = Person.valueOf("Stefan", "Petersson", "stefan@example.com");
        final Person markus = Person.valueOf("Markus", "Gustavsson", "markus@example.com");
        final Person ian = Person.valueOf("Ian", "Vännman", "ian@example.com");
        final Person marten = Person.valueOf("Mårten", "Gustafson", "marten@example.com");

        final List<Person> actualPersons = RESOURCE_EXTENSION.target("/persons").request().get(new GenericType<List<Person>>() {});

        final List<Person> expectedPersons = List.of(marten, markus, stefan, ian);
        assertEquals(expectedPersons, actualPersons);
    }

    @Test
    public void readExistingPerson() {
        final Person actualPerson = RESOURCE_EXTENSION.target("/persons/marten@example.com").request().get(Person.class);

        final Person expectedPerson = Person.valueOf("Mårten", "Gustafson", "marten@example.com");
        assertEquals(expectedPerson, actualPerson);
    }

    @Test
    public void readNonExistentPerson() {
        final Response response = RESOURCE_EXTENSION.target("/persons/user@example.com").request().get();

        final int expectedRespStatus = 0; // FIXME: Pick an HTTP response status you think is most suitable here.
        assertEquals(expectedRespStatus, response.getStatus());
    }

    @Test
    public void createNewPerson() {
        final Person payload = Person.valueOf("Mårten", "Gustafson", "marten@example.com");
        final Response response = RESOURCE_EXTENSION.target("/persons").request().post(Entity.json(payload));

        final int expectedRespStatus = 0; // FIXME: Pick an HTTP response status you think is most suitable here.
        assertEquals(expectedRespStatus, response.getStatus());
        assertEquals("/persons/marten@example.com", response.getLocation().getPath());
    }

    @Test
    public void createPersonThatAlreadyExists() {
        final Person payload = Person.valueOf("Mårten", "Gustafson", "marten@example.com");
        final Response response = RESOURCE_EXTENSION.target("/persons").request().post(Entity.json(payload));

        final int expectedRespStatus = 0; // FIXME: Pick an HTTP response status you think is most suitable here.
        assertEquals(expectedRespStatus, response.getStatus());
    }

    @Test
    public void updateExistingPerson() {
        final Person payload = Person.valueOf("Mårten2", "Gustafson2", "marten@example.com");
        final Person actualPerson = RESOURCE_EXTENSION.target("/persons/marten@example.com").request()
                .put(Entity.json(payload), Person.class);

        final Person expectedPerson = Person.valueOf("Mårten2", "Gustafson2", "marten@example.com");
        assertEquals(expectedPerson, actualPerson);
    }

    @Test
    public void updateNonExistentPerson() {
        final Person payload = Person.valueOf("Mårten", "Gustafson", "marten@example.com");
        final Response response = RESOURCE_EXTENSION.target("/persons/user@example.com").request().put(Entity.json(payload));

        final int expectedRespStatus = 0; // FIXME: Pick an HTTP response status you think is most suitable here.
        assertEquals(expectedRespStatus, response.getStatus());
    }

    @Test
    public void deleteExistingPerson() {
        final Response response = RESOURCE_EXTENSION.target("/persons/marten@example.com").request().delete();

        final int expectedRespStatus = 0; // FIXME: Pick an HTTP response status you think is most suitable here.
        assertEquals(expectedRespStatus, response.getStatus());
    }

    @Test
    public void deleteNonExistentPerson() {
        final Response response = RESOURCE_EXTENSION.target("/persons/user@example.com").request().delete();

        final int expectedRespStatus = 0; // FIXME: Pick an HTTP response status you think is most suitable here.
        assertEquals(expectedRespStatus, response.getStatus());
    }
}
