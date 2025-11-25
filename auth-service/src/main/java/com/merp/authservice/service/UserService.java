package com.merp.authservice.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.merp.authservice.client.HrClient;
import com.merp.authservice.dto.CreateEmployeeInternalRequest;
import com.merp.authservice.dto.CreateUserRequest;
import com.merp.authservice.dto.UserResponse;
import com.merp.authservice.entity.Role;
import com.merp.authservice.entity.User;
import com.merp.authservice.repository.AuthRepository;
import com.merp.authservice.repository.RoleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final HrClient hrClient;

    public UserResponse createUser(CreateUserRequest request) {
        // Lookup role by name
        Role role = roleRepository.findByName(request.getRole())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        // Create User
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(role); // ✅ now this works

        authRepository.save(user);

        // ✅ If employee details are provided, create employee in HR service
        if (request.getFirstName() != null && request.getLastName() != null) {
            System.out.println("🔵 Employee details detected - attempting to create employee in HR service");
            System.out.println("🔵 User ID: " + user.getId());
            System.out.println("🔵 Name: " + request.getFirstName() + " " + request.getLastName());
            
            CreateEmployeeInternalRequest employeeRequest = new CreateEmployeeInternalRequest();
            employeeRequest.setUserId(user.getId());
            employeeRequest.setFirstName(request.getFirstName());
            employeeRequest.setLastName(request.getLastName());
            employeeRequest.setEmail(request.getEmail());
            employeeRequest.setDepartment(request.getDepartment());
            employeeRequest.setJobTitle(request.getJobTitle());
            employeeRequest.setSalary(request.getSalary());
            
            try {
                System.out.println("🔵 Calling HR service...");
                hrClient.createEmployee(employeeRequest);
                System.out.println("✅ Successfully created employee in HR service");
            } catch (Exception e) {
                // Log error but don't fail user creation
                System.err.println("❌ Failed to create employee in HR service: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("⚠️ No employee details provided - skipping employee creation");
        }

        return new UserResponse(user.getId(), user.getEmail(), role.getName());
    }
}
