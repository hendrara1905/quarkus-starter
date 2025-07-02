package com.example.modularapp.auth;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.example.modularapp.user.User;
import com.example.modularapp.user.UserRepository;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.jwt.Claims;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class AuthService {
    private static  final Map<String, User> users = new HashMap<>();

    @Inject
    private UserRepository userRepository;

    @Inject
    private LogoutConfig logoutConfig;

    public User authenticate(String username, String password) {
        User user = userRepository.find("username", username).firstResult();
        if (user == null) return null;

        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), user.password);
        return result.verified ? user : null;
    }

    public String createToken(User user , String issuer) {
        return Jwt.issuer(issuer)
                .upn(user.username)
                .claim(Claims.groups.name(), user.role.name)
                .claim("preferred_username", user.username)
                .expiresIn(logoutConfig.durationSession())
                .sign();
    }

}
