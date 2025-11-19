package com.merp.hrservice.service;



import com.merp.hrservice.client.AuthClient;
import com.merp.hrservice.dto.CreateEmployeeRequest;
import com.merp.hrservice.dto.CreateUserRequest;
import com.merp.hrservice.dto.UserResponse;
import com.merp.hrservice.entity.Employee;
import com.merp.hrservice.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
