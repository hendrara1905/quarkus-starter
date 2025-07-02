package com.example.modularapp.book;

import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;
import jakarta.annotation.security.RolesAllowed;


import java.util.List;

@Path("/cms/books")
//@RolesAllowed("admin")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({"ADMIN","USER"})
public class BookResource {

    private static final Logger LOGGER = Logger.getLogger(BookResource.class);

    @Inject
    BookService service;

    @GET
    public List<Book> list() {
         System.out.println("Received List BookDTO: ");
       
        return service.listAll();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(@Valid BookDTO dto) {
        return Response.status(Response.Status.CREATED).entity(service.add(dto)).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, @Valid BookDTO dto) {
        Book updated = service.update(id, dto);
        if (updated == null) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(updated).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        return service.delete(id) ? Response.noContent().build() : Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") Long id) {
        Book book = service.findById(id);
        return (book == null) ? Response.status(Response.Status.NOT_FOUND).build() : Response.ok(book).build();
    }
}
