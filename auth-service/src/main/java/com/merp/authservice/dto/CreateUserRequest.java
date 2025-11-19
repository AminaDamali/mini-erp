package com.merp.authservice.dto;

import lombok.Data;

@Data
public class CreateUserRequest {

    private String email;
    private String password;

    private Long id;
    private String role;

}
