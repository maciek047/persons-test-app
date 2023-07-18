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
        final Person stefan = createPerson("Stefan", "Petersson", "stefan@example.com");
        final Person markus = createPerson("Markus", "Gustavsson", "markus@example.com");
        final Person ian = createPerson("Ian", "Vännman", "ian@example.com");
        final Person marten = createPerson("Mårten", "Gustafson", "marten@example.com");

        savePersons(stefan, markus, ian, marten);

        final List<Person> actualPersons = getRequest("/persons").readEntity(new GenericType<List<Person>>() {
        });

        final List<Person> expectedPersons = List.of(marten, markus, stefan, ian);
        assertEquals(expectedPersons, actualPersons);
    }

    @Test
    public void readExistingPerson() {
        final Person expectedPerson = createPerson("Mårten", "Gustafson", "marten@example.com");
        savePersons(expectedPerson);

        final Person actualPerson = getRequest("/persons/marten@example.com").readEntity(Person.class);
        assertEquals(expectedPerson, actualPerson);
    }

    @Test
    public void readNonExistentPerson() {
        final Response response = getRequest("/persons/user@example.com");

        final int expectedRespStatus = HttpStatus.NOT_FOUND_404;
        assertEquals(expectedRespStatus, response.getStatus());
    }

    @Test
    public void createNewPerson() {
        String email = "marten@example.com";
        final Person payload = createPerson("Mårten", "Gustafson", email);
        final Response response = postRequest("/persons", payload);

        final int expectedRespStatus = HttpStatus.CREATED_201;
        assertEquals(expectedRespStatus, response.getStatus());
        int emailHash = Objects.hash(email);
        assertEquals("/persons/" + emailHash, response.getLocation().getPath());
    }

    @Test
    public void createPersonThatAlreadyExists() {
        final Person payload = createPerson("Mårten", "Gustafson", "marten@example.com");
        savePersons(payload);

        final Response response = postRequest("/persons", payload);

        final int expectedRespStatus = HttpStatus.CONFLICT_409;
        assertEquals(expectedRespStatus, response.getStatus());
    }

    @Test
    public void updateExistingPerson() {
        final Person originalPerson = createPerson("Mårten", "Gustafson", "marten@example.com");
        savePersons(originalPerson);
        final Person payload = createPerson("Mårten2", "Gustafson2", "marten@example.com");

        final Person actualPerson = putRequest("/persons/marten@example.com", payload).readEntity(Person.class);

        final Person expectedPerson = createPerson("Mårten2", "Gustafson2", "marten@example.com");
        assertEquals(expectedPerson, actualPerson);
    }

    @Test
    public void updateNonExistentPerson() {
        final Person payload = createPerson("Mårten", "Gustafson", "marten@example.com");
        final Response response = putRequest("/persons/user@example.com", payload);

        final int expectedRespStatus = HttpStatus.NOT_FOUND_404;
        assertEquals(expectedRespStatus, response.getStatus());
    }

    @Test
    public void deleteExistingPerson() {
        final Person payload = createPerson("Mårten", "Gustafson", "marten@example.com");
        savePersons(payload);
        final Response response = deleteRequest("/persons/marten@example.com");

        final int expectedRespStatus = HttpStatus.NO_CONTENT_204;
        assertEquals(expectedRespStatus, response.getStatus());
    }

    @Test
    public void deleteNonExistentPerson() {
        final Response response = deleteRequest("/persons/user@example.com");

        final int expectedRespStatus = HttpStatus.NOT_FOUND_404;
        assertEquals(expectedRespStatus, response.getStatus());
    }

    private void savePersons(Person... persons) {
        for (Person person : persons) {
            storage.save(person);
        }
    }

    private Person createPerson(String firstName, String lastName, String email) {
        return Person.valueOf(firstName, lastName, email);
    }

    private Response getRequest(String target) {
        return RESOURCE_EXTENSION.target(target).request().get();
    }

    private Response postRequest(String target, Person payload) {
        return RESOURCE_EXTENSION.target(target).request().post(Entity.json(payload));
    }

    private Response putRequest(String target, Person payload) {
        return RESOURCE_EXTENSION.target(target).request().put(Entity.json(payload));
    }

    private Response deleteRequest(String target) {
        return RESOURCE_EXTENSION.target(target).request().delete();
    }

}
