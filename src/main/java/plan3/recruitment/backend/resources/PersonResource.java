package plan3.recruitment.backend.resources;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import plan3.recruitment.backend.model.Person;
import plan3.recruitment.backend.model.PersonStorage;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("persons")
@Produces(PersonResource.APPLICATION_JSON_UTF8)
@Consumes(PersonResource.APPLICATION_JSON_UTF8)
public class PersonResource {

    public static final String APPLICATION_JSON_UTF8 = APPLICATION_JSON + "; charset=utf-8";
    private static final String EMAIL_PARAM = "email";
    private static final String EMAIL_PATH_PARAM = '{' + EMAIL_PARAM + '}';
    private final PersonStorage storage = null; // FIXME: Use your PersonStorage implementation here

    @POST
    public Response create(final Person person) {
        return null;
    }

    @GET
    @Path(EMAIL_PATH_PARAM)
    public Response read(@PathParam(EMAIL_PARAM) final String email) {
        return null;
    }

    @PUT
    @Path(EMAIL_PATH_PARAM)
    public Response update(@PathParam(EMAIL_PARAM) final String email, final Person person) {
        return null;
    }

    @DELETE
    @Path(EMAIL_PATH_PARAM)
    public Response delete(@PathParam(EMAIL_PARAM) final String email) {
        return null;
    }

    @GET
    public Response list() {
        return null;
    }
}
