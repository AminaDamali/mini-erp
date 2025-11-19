package com.merp.authservice.service;

import com.merp.authservice.dto.CreateUserRequest;
import com.merp.authservice.dto.UserResponse;
import com.merp.authservice.entity.Role;
import com.merp.authservice.entity.User;
import com.merp.authservice.repository.AuthRepository;
import com.merp.authservice.repository.RoleRepository;
import com.merp.authservice.security.JwtUtil;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

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

        return new UserResponse(user.getId(), user.getEmail(), role.getName());
    }
}
