package com.merp.authservice.dto;

import lombok.Data;

@Data
public class CreateUserRequest {

    private String email;
    private String password;

    private Long id;
    private String role;
    
    // Employee details (optional, for creating both user and employee)
    private String firstName;
    private String lastName;
    private String department;
    private String jobTitle;
    private Double salary;

}
