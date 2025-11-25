package com.merp.authservice.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.merp.authservice.entity.User;
import com.merp.authservice.repository.AuthRepository;
import com.merp.authservice.security.JwtUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    public String login(String email, String password) {

        User user = authRepository.findByEmail(email);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            // ✅ Generate token with user's actual role
            return jwtUtil.generateToken(user.getEmail(), user.getId(), List.of(user.getRole().getName()));
        }

      return null;
    }
}
