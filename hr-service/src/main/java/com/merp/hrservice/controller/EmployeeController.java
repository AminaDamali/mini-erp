package com.merp.hrservice.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.merp.hrservice.dto.CreateEmployeeInternalRequest;
import com.merp.hrservice.dto.CreateEmployeeRequest;
import com.merp.hrservice.dto.EmployeeResponse;
import com.merp.hrservice.service.EmployeeService;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }
    
    @PostMapping
    public void createEmployee(@RequestBody CreateEmployeeRequest request) {
        employeeService.createEmployee(request);
    }
    
    /**
     * Internal endpoint called by auth-service to create employee
     */
    @PostMapping("/internal")
    public EmployeeResponse createEmployeeInternal(@RequestBody CreateEmployeeInternalRequest request) {
        return employeeService.createEmployeeInternal(request);
    }
}
