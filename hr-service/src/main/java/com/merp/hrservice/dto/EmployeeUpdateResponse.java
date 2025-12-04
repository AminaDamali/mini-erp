package com.merp.hrservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeUpdateResponse {
    private Long id;
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String department;
    private String jobTitle;
    private Double salary;
    private String phone;
    private String address;
    private String city;
    private String contractType;
}


