package fr.rest.client.fakeapi;

import fr.rest.client.fakeapi.model.Todo;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;

@Path("/todos")
@RegisterRestClient
public interface FakeApiService {

    @GET
    @Produces("application/json")
    List<Todo> getTodos();
}