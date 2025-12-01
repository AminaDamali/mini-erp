package com.merp.hrservice.dto;

import lombok.Data;

@Data
public class EmployeeUpdateRequest {
    private String firstName;
    private String lastName;
    private String department;
    private String jobTitle;
    private Double salary;
}


