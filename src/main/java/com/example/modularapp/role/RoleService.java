package com.example.modularapp.role;

import com.example.exception.NotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class RoleService {
    @Inject
    RoleRepository roleRepository;

    public void create(RoleDTO dto) {
        Role role = new Role();
        role.name = dto.name;
        roleRepository.persist(role);
    }

    public List<Role> list() {
        return roleRepository.listAll();
    }

    public Role findById(Long id) {
        Role role = roleRepository.findById(id);
        if (role == null) throw new NotFoundException("Role not found");
        return role;
    }
}
