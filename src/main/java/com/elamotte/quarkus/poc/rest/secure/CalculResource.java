package com.elamotte.quarkus.poc.rest.secure;

import com.elamotte.quarkus.poc.rest.annotation.JWTTokenNeeded;
import com.elamotte.quarkus.poc.rest.model.Calcul;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.ArrayList;
import java.util.List;

@Path("/calcul")
@RequestScoped
@JWTTokenNeeded
public class CalculResource {

    @GET()
    @RolesAllowed({"Notaire"})
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCalculs(@Context SecurityContext ctx) {
        List<Calcul> calculs = new ArrayList<>();
        Calcul calcul = new Calcul();
        calcul.setFormule("formule");
        calcul.setOperateur("plus");
        calcul.setValue("valeur");
        calculs.add(calcul);

        return Response.ok(calculs).build();

    }
}
