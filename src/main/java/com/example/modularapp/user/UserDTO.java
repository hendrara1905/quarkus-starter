package com.example.modularapp.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UserDTO {

    public Long id;

    @NotBlank
    public String username;

    @NotBlank
    public String password;

    @NotNull
    public Long roleId;

    public String roleName;

}
