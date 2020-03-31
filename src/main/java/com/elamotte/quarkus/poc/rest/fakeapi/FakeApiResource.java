package com.elamotte.quarkus.poc.rest.fakeapi;

import com.elamotte.quarkus.poc.rest.fakeapi.model.Todo;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/fakeapi")
@Produces(MediaType.APPLICATION_JSON)
public class FakeApiResource {

    @Inject
    @RestClient
    com.elamotte.quarkus.poc.rest.client.fakeapi.FakeApiService fakeApiService;


    @GET
    @Path("/todos")
    public Response getTodos() {
        List<Todo> todos = fakeApiService.getTodos();
        return Response.ok(todos).build();
    }
}