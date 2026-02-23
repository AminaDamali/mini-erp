package com.merp.hrservice.controller;

import com.merp.hrservice.dto.DepartmentUpdateRequest;
import com.merp.hrservice.service.DepartmentService;

import feign.Request;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



import org.springframework.web.bind.annotation.*;

import com.merp.hrservice.dto.CreateDepartmentRequest;

import com.merp.hrservice.dto.DepartmentResponse;



@RestController
@RequiredArgsConstructor
@RequestMapping("/api/departments")
public class DepartmentController {
    private final DepartmentService departmentService ;
    @PostMapping("/create")
    public DepartmentResponse createDepartment (@RequestBody CreateDepartmentRequest request)
    {return departmentService.createDepartment(request);}

    @PutMapping("/{id}")
    public DepartmentResponse updateDepartment (Long id, @RequestBody DepartmentUpdateRequest request)
    {return departmentService.updateDepartment(id,request);}

}
