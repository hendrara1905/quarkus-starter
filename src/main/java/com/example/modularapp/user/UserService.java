package com.example.modularapp.user;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.example.exception.NotFoundException;
import com.example.modularapp.role.Role;
import com.example.modularapp.role.RoleRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class UserService {
    @Inject
    UserRepository userRepository;

    @Inject
    RoleRepository roleRepository;

    public void create(UserDTO dto) {
        Role role = roleRepository.findById(dto.roleId);
        if (role == null) throw new NotFoundException("Role not found");

        User user = new User();
        user.username = dto.username;
        user.password = BCrypt.withDefaults().hashToString(12, dto.password.toCharArray());
        user.role = role;

        userRepository.persist(user);
    }

    public List<UserDTO> list() {

        return userRepository.listAll().stream().map(user ->
                {
                    UserDTO dto = new UserDTO();
                    dto.id = user.id;
                    dto.username = user.username;
                    dto.roleName = user.role != null ? user.role.name : null;
                    return dto;
                }
                ).collect(Collectors.toList());

    }
}
