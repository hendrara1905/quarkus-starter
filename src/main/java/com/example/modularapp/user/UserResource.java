package com.example.modularapp.user;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({"ADMIN"})
public class UserResource {
    @Inject
    UserService userService;

    @POST
    public void createUser(@Valid UserDTO dto) {
        userService.create(dto);
    }

    @GET
    public List<UserDTO> listUsers() {
        return userService.list();
    }
}
