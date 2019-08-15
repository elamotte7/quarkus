package com.elamotte.quarkus.poc.rest;

import com.elamotte.quarkus.poc.rest.annotation.JWTTokenNeeded;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.DenyAll;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

@Path("/hello")
@RequestScoped
@Produces(MediaType.TEXT_PLAIN)
public class HelloWorldResource {

    private static final Logger LOG = LoggerFactory.getLogger(HelloWorldResource.class);

    @ConfigProperty(name = "greeting.message")
    String message;

    @ConfigProperty(name = "greeting.name")
    String name;

    @GET
    public String helloWorldWithoutToken() {
        return this.message;
    }

    @GET
    @JWTTokenNeeded
    @Path("/jwt")
    public String helloWorldWithToken(@Context SecurityContext ctx) {

        LOG.info("hello with jwt");
        LOG.info(ctx.getAuthenticationScheme() + " " + ctx.getUserPrincipal() + " " + ctx.isUserInRole(""));
        return "hello World with jwt token valid!!";
    }

    @GET
    @JWTTokenNeeded
    @DenyAll
    @Path("/jwt/permit-all")
    public String helloWorldDenyAll(@Context SecurityContext ctx) {

        LOG.info("hello with jwt");
        LOG.info(ctx.getAuthenticationScheme() + " " + ctx.getUserPrincipal() + " " + ctx.isUserInRole(""));
        return "hello World with jwt token valid!!";
    }

    @GET
    @JWTTokenNeeded
    @DenyAll
    @Path("/jwt/deny-all")
    public String helloWorldPermitAll(@Context SecurityContext ctx) {

        LOG.info("hello with jwt");
        LOG.info(ctx.getAuthenticationScheme() + " " + ctx.getUserPrincipal() + " " + ctx.isUserInRole(""));
        return "hello World with jwt token valid!!";
    }

}