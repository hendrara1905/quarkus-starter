package com.example.modularapp.role;

import jakarta.validation.constraints.NotBlank;

public class RoleDTO {
    @NotBlank
    public String name;

    public Long id;
}
