package plan3.recruitment.backend.resources;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import plan3.recruitment.backend.model.Person;
import plan3.recruitment.backend.model.PersonStorage;
import plan3.recruitment.backend.model.PersonStorageImpl;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.net.URI;

@Path("persons")
@Produces(PersonResource.APPLICATION_JSON_UTF8)
@Consumes(PersonResource.APPLICATION_JSON_UTF8)
public class PersonResource {

    public static final String APPLICATION_JSON_UTF8 = APPLICATION_JSON + "; charset=utf-8";
    private static final String EMAIL_PARAM = "email";
    private static final String EMAIL_PATH_PARAM = '{' + EMAIL_PARAM + '}';

    public PersonResource(PersonStorage storage) {
        this.storage = storage;
    }

    public PersonResource() {
        this(new PersonStorageImpl());
    }

    private final PersonStorage storage;

    @POST
    public Response create(final Person person) {
        try {
            storage.save(person);
            return Response.created(URI.create("/persons/" + person.hashCode())).entity(person).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.CONFLICT).build();
        }
    }

    @GET
    @Path(EMAIL_PATH_PARAM)
    public Response read(@PathParam(EMAIL_PARAM) final String email) {
        Person person = storage.fetch(email)
                .orElseThrow(() -> new WebApplicationException("Person not found", Response.Status.NOT_FOUND));
        return Response.ok(person).build();
    }

    @PUT
    @Path(EMAIL_PATH_PARAM)
    public Response update(@PathParam(EMAIL_PARAM) final String email, final Person person) {
        try {
            storage.update(person);
            return Response.ok(person).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path(EMAIL_PATH_PARAM)
    public Response delete(@PathParam(EMAIL_PARAM) final String email) {
        Person person = storage.fetch(email)
                .orElseThrow(() -> new WebApplicationException("Person not found", Response.Status.NOT_FOUND));
        storage.remove(person);
        return Response.noContent().build();
    }

    @GET
    public Response list() {
        return Response.ok(storage.list()).build();
    }
}
