package com.merp.hrservice.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.merp.hrservice.dto.CreateEmployeeInternalRequest;
import com.merp.hrservice.dto.CreateEmployeeRequest;
import com.merp.hrservice.dto.EmployeeResponse;
import com.merp.hrservice.dto.EmployeeUpdateRequest;
import com.merp.hrservice.dto.EmployeeUpdateResponse;
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
    
    /**
     * Update employee endpoint - Only ADMIN and HR can access
     */
    @PutMapping("/{id}")
    public EmployeeUpdateResponse updateEmployee(
            @PathVariable Long id, 
            @RequestBody EmployeeUpdateRequest request) {
        return employeeService.updateEmployee(id, request);
    }

    /**
     * Get all employees - Only ADMIN and HR can access
     */
    @GetMapping
    public List<EmployeeResponse> getAllEmployees() {
        return employeeService.getAllEmployees();
    }



}
