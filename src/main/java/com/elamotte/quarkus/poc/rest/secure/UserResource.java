package com.elamotte.quarkus.poc.rest.secure;

import com.elamotte.quarkus.poc.rest.annotation.JWTTokenNeeded;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Path("/secure/user")
@Produces(MediaType.APPLICATION_JSON)
@JWTTokenNeeded
@RolesAllowed({"role1", "role2"})
public class UserResource {

    @GET
    public Response get(@Context SecurityContext ctx) {
        return Response.ok("The user is granted to acces to user resource").build();
    }
}
