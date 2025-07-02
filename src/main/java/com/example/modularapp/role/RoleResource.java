package com.example.modularapp.role;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/roles")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed("ADMIN")
public class RoleResource {
    @Inject
    RoleService roleService;

    @POST
    public void create(@Valid RoleDTO dto) {
        roleService.create(dto);
    }

    @GET
    public List<Role> list() {
        return roleService.list();
    }

    @GET
    @Path("/{id}")
    public Role find(@PathParam("id") Long id) {
        return roleService.findById(id);
    }
}
