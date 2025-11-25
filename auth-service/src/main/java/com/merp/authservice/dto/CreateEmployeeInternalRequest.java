package com.merp.authservice.dto;

import lombok.Data;

@Data
public class CreateEmployeeInternalRequest {
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String department;
    private String jobTitle;
    private Double salary;
}
