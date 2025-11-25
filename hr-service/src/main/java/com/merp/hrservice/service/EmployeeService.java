package com.merp.hrservice.service;



import org.springframework.stereotype.Service;

import com.merp.hrservice.client.AuthClient;
import com.merp.hrservice.dto.CreateEmployeeInternalRequest;
import com.merp.hrservice.dto.CreateEmployeeRequest;
import com.merp.hrservice.dto.CreateUserRequest;
import com.merp.hrservice.dto.EmployeeResponse;
import com.merp.hrservice.dto.UserResponse;
import com.merp.hrservice.entity.Employee;
import com.merp.hrservice.repository.EmployeeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final AuthClient authClient;

    public Employee createEmployee(CreateEmployeeRequest request) {

        // 1️⃣ Create login account in Auth-Service
        CreateUserRequest userReq = new CreateUserRequest();
        userReq.setEmail(request.getEmail());
        userReq.setPassword("temp1234");      // temporary password
        userReq.setRole("EMPLOYEE");

        UserResponse userRes = authClient.createUser(userReq);

        // 2️⃣ Create employee record in HR DB
        Employee emp = new Employee();
        emp.setFirstName(request.getFirstName());
        emp.setLastName(request.getLastName());
        emp.setEmail(request.getEmail());
        emp.setDepartment(request.getDepartment());
        emp.setJobTitle(request.getJobTitle());
        emp.setSalary(request.getSalary());
        emp.setUserId(userRes.getId());   // ✅ link to Auth user

        return employeeRepository.save(emp);
    }

    /**
     * Internal endpoint for creating employee from auth-service
     */
    public EmployeeResponse createEmployeeInternal(CreateEmployeeInternalRequest request) {
        System.out.println("🟢 HR Service: Received employee creation request");
        System.out.println("🟢 User ID: " + request.getUserId());
        System.out.println("🟢 Name: " + request.getFirstName() + " " + request.getLastName());
        
        Employee emp = new Employee();
        emp.setUserId(request.getUserId());
        emp.setFirstName(request.getFirstName());
        emp.setLastName(request.getLastName());
        emp.setEmail(request.getEmail());
        emp.setDepartment(request.getDepartment());
        emp.setJobTitle(request.getJobTitle());
        emp.setSalary(request.getSalary());

        Employee saved = employeeRepository.save(emp);
        System.out.println("✅ HR Service: Employee saved with ID: " + saved.getId());

        return new EmployeeResponse(
            saved.getId(),
            saved.getUserId(),
            saved.getFirstName(),
            saved.getLastName(),
            saved.getEmail(),
            saved.getDepartment(),
            saved.getJobTitle()
        );
    }
}
