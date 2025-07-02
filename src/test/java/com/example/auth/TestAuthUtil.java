package com.example.auth;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.example.modularapp.role.Role;
import com.example.modularapp.role.RoleRepository;
import com.example.modularapp.user.User;
import com.example.modularapp.user.UserRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.Map;

@ApplicationScoped
public class TestAuthUtil {

    @Inject
    UserRepository userRepository;

    @Inject
    RoleRepository roleRepository;

    @Transactional
    public String getToken(String username, String password, String roleName) {
        Role role = roleRepository.find("name", roleName).firstResult();
        if (role == null) {
            role = new Role();
            role.name = roleName;
            roleRepository.persist(role);
        }

        User user = userRepository.find("username", username).firstResult();
        if (user == null) {
            user = new User();
            user.username = username;
//            user.password = at.favre.lib.crypto.bcrypt.BCrypt.withDefaults()
//                    .hashToString(12, password.toCharArray());
            user.password = BCrypt.withDefaults().hashToString(12, password.toCharArray());

            user.role = role;
            userRepository.persist(user);
        }else {
            // Always ensure password match with what we expect in test
            user.password = BCrypt.withDefaults().hashToString(12, password.toCharArray());
            userRepository.persist(user);
        }

        System.out.println("USER: " + user);
        System.out.println("ROLE: " + role);

        System.out.println("USERNAME: " + username + " USER DB " + user.username);
        System.out.println("PASSWORD: " + password + " PASS DB " + user.password);


//        return RestAssured
//                .given()
//                .contentType(ContentType.JSON)
//                .body(Map.of("username", username, "password", password))
//                .when()
//                .post("/auth/login")
//                .then()
//                .statusCode(200)
//                .extract()
//                .path("token");

        Response response = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(Map.of("username", username, "password", password))
                .when()
                .post("/auth/login");

        System.out.println("STATUS: " + response.statusCode());
        System.out.println("BODY: " + response.getBody().asString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Login failed");
        }

        return response.jsonPath().getString("token");

    }
}
