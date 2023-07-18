package plan3.recruitment.backend.resources;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import plan3.recruitment.backend.model.Person;

import java.util.List;
import java.util.Objects;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import plan3.recruitment.backend.model.PersonStorageImpl;

@ExtendWith(DropwizardExtensionsSupport.class)
public class PersonsResourceTest {

    private final PersonStorageImpl storage = new PersonStorageImpl();
    private final PersonResource personResource = new PersonResource(storage);

    private final ResourceExtension RESOURCE_EXTENSION = ResourceExtension.builder()
            .addResource(personResource)
            .build();

    @BeforeEach
    public void setup() {
        storage.list().forEach(storage::remove);
    }

    @Test
    public void listSortedOnLastname() {
        final Person stefan = Person.valueOf("Stefan", "Petersson", "stefan@example.com");
        final Person markus = Person.valueOf("Markus", "Gustavsson", "markus@example.com");
        final Person ian = Person.valueOf("Ian", "Vännman", "ian@example.com");
        final Person marten = Person.valueOf("Mårten", "Gustafson", "marten@example.com");

        storage.save(stefan);
        storage.save(markus);
        storage.save(ian);
        storage.save(marten);

        final List<Person> actualPersons = RESOURCE_EXTENSION.target("/persons").request().get(new GenericType<List<Person>>() {
        });

        final List<Person> expectedPersons = List.of(marten, markus, stefan, ian);
        assertEquals(expectedPersons, actualPersons);
    }

    @Test
    public void readExistingPerson() {
        final Person expectedPerson = Person.valueOf("Mårten", "Gustafson", "marten@example.com");
        storage.save(expectedPerson);

        final Person actualPerson = RESOURCE_EXTENSION.target("/persons/marten@example.com").request().get(Person.class);
        assertEquals(expectedPerson, actualPerson);
    }

    @Test
    public void readNonExistentPerson() {
        final Response response = RESOURCE_EXTENSION.target("/persons/user@example.com").request().get();

        final int expectedRespStatus = HttpStatus.NOT_FOUND_404;
        assertEquals(expectedRespStatus, response.getStatus());
    }

    @Test
    public void createNewPerson() {
        String email = "marten@example.com";
        final Person payload = Person.valueOf("Mårten", "Gustafson", email);
        final Response response = RESOURCE_EXTENSION.target("/persons").request().post(Entity.json(payload));

        final int expectedRespStatus = HttpStatus.CREATED_201;
        assertEquals(expectedRespStatus, response.getStatus());
        int emailHash = Objects.hash(email);
        assertEquals("/persons/" + emailHash, response.getLocation().getPath());
    }

    @Test
    public void createPersonThatAlreadyExists() {
        final Person payload = Person.valueOf("Mårten", "Gustafson", "marten@example.com");
        storage.save(payload);

        final Response response = RESOURCE_EXTENSION.target("/persons").request().post(Entity.json(payload));

        final int expectedRespStatus = HttpStatus.CONFLICT_409;
        assertEquals(expectedRespStatus, response.getStatus());
    }

    @Test
    public void updateExistingPerson() {
        final Person originalPerson = Person.valueOf("Mårten", "Gustafson", "marten@example.com");
        storage.save(originalPerson);
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

        final int expectedRespStatus = HttpStatus.NOT_FOUND_404;
        assertEquals(expectedRespStatus, response.getStatus());
    }

    @Test
    public void deleteExistingPerson() {
        final Person payload = Person.valueOf("Mårten", "Gustafson", "marten@example.com");
        storage.save(payload);
        final Response response = RESOURCE_EXTENSION.target("/persons/marten@example.com").request().delete();

        final int expectedRespStatus = HttpStatus.NO_CONTENT_204;
        assertEquals(expectedRespStatus, response.getStatus());
    }

    @Test
    public void deleteNonExistentPerson() {
        final Response response = RESOURCE_EXTENSION.target("/persons/user@example.com").request().delete();

        final int expectedRespStatus = HttpStatus.NOT_FOUND_404;
        assertEquals(expectedRespStatus, response.getStatus());
    }
}
