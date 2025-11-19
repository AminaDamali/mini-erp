package com.merp.hrservice.dto;

import lombok.Data;
@Data
public class CreateEmployeeRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String department;
    private String jobTitle;
    private Double salary;
}
