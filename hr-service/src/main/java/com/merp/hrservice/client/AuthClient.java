package com.merp.hrservice.client;
import com.merp.hrservice.dto.CreateUserRequest;
import com.merp.hrservice.dto.UserResponse;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
@Component
@FeignClient(name = "auth-service")
public interface AuthClient {
    @GetMapping("/api/internal/users/{id}")
    UserResponse getUserById(@PathVariable Long id);

    @PostMapping("/api/internal/users/{id}/role")
    void updateUserRole(@PathVariable Long id, String newRole);

    @PostMapping("/api/internal/users")
    UserResponse createUser(@RequestBody CreateUserRequest request);
}
