package com.merp.hrservice.dto;

import lombok.Data;

@Data
public class CreateDepartmentRequest {
    private int id;
    private String name;
    private String city;

}
