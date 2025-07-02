package com.example.modularapp.auth;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.example.modularapp.role.Role;
import com.example.modularapp.role.RoleRepository;
import com.example.modularapp.user.User;
import com.example.modularapp.user.UserDTO;
import com.example.modularapp.user.UserRepository;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    RoleRepository roleRepository;

    @Inject
    UserRepository userRepository;
    // Simulasi user database
    private static final Map<String, User> USERS = new HashMap<>();

    @Inject
    AuthService authService;

    @Inject
    LogoutService logoutService;

    private String issuer = "http://example.org/issuer";

    private Duration sessionDuration= Duration.ofHours(2);

    @POST
    @Path("/login")
    public Response login(User inputUser) {

        User user  = authService.authenticate(inputUser.username, inputUser.password);

        if (user != null) {
            String token = authService.createToken(user,this.issuer);

            // JSON response
            Map<String, Object> responseBody = Map.of(
                    "token", token,
                    "username", user.username,
                    "role", user.role.name,
                    "duration", this.sessionDuration
            );
            return Response.ok(responseBody).build();

        }
        return Response.status(Response.Status.UNAUTHORIZED)
                .entity(Map.of("message","Username dan Password Salah")).build();
    }

    @POST
    @Path("/register")
    @PermitAll
    @Transactional
    public Response register(@Valid UserDTO dto) {
        Role defaultRole = roleRepository.find("name", "USER").firstResult();
        if (defaultRole == null) {
            defaultRole = new Role();
            defaultRole.name = "USER";
            roleRepository.persist(defaultRole);
        }

        User user = new User();
        user.username = dto.username;
        user.password = BCrypt.withDefaults().hashToString(12, dto.password.toCharArray());
        user.role = defaultRole;
        userRepository.persist(user);

        return Response.status(Response.Status.CREATED).build();
    }

    @POST
    @Path("/refresh")
    @RolesAllowed({"USER", "ADMIN"})
    public Response refresh(@Context SecurityContext ctx) {
        String username = ctx.getUserPrincipal().getName();
        User user = userRepository.find("username", username).firstResult();

        if (user == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }


        String token = authService.createToken(user, this.issuer);
        return Response.ok(Map.of("token", token)).build();
    }

    @POST
    @Path("/logout")
    @RolesAllowed({"USER", "ADMIN"})
    public Response logout(@Context SecurityContext ctx, @HeaderParam("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "Missing or invalid Authorization header"))
                    .build();
        }

        String token = authHeader.substring("Bearer ".length());
        logoutService.blacklistToken(token);

        return Response.ok(Map.of("message", "Logout successful")).build();
    }


}
