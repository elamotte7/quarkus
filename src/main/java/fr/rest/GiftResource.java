package fr.rest;

import fr.hibernate.panache.model.Gift;
import io.quarkus.panache.common.Sort;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.enterprise.context.RequestScoped;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/gifts")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GiftResource {
    @GET
    public List<Gift> get() {
        return Gift.listAll(Sort.by("name"));
    }

    @GET
    @Path("/{id}")
    public Gift getSingle(@PathParam Long id) {
        Gift entity = Gift.findById(id);
        if (entity == null) {
            throw new WebApplicationException("Gift with id of " + id + " does not exist.", 404);
        }
        return entity;
    }

    @POST
    @Transactional
    public Response create(Gift gift) {
        if (gift.id != null) {
            throw new WebApplicationException("Id was invalidly set on request.", 422);
        }

        gift.persist();
        return Response.ok(gift).status(201).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Gift update(@PathParam Long id, Gift gift) {
        if (gift.name == null) {
            throw new WebApplicationException("Gift Name was not set on request.", 422);
        }

        Gift entity = Gift.findById(id);

        if (entity == null) {
            throw new WebApplicationException("Gift with id of " + id + " does not exist.", 404);
        }

        entity.name = gift.name;

        return entity;
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(@PathParam Long id) {
        Gift entity = Gift.findById(id);
        if (entity == null) {
            throw new WebApplicationException("Gift with id of " + id + " does not exist.", 404);
        }
        entity.delete();
        return Response.status(204).build();
    }
}
