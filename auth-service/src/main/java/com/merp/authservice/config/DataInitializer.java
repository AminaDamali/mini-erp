package com.merp.authservice.config;

import com.merp.authservice.entity.Role;
import com.merp.authservice.entity.User;
import com.merp.authservice.repository.AuthRepository;
import com.merp.authservice.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final AuthRepository authRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // 1️⃣ Create ADMIN role if not exists
        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseGet(() -> {
                    Role r = new Role();
                    r.setName("ADMIN");
                    r.setDescription("System administrator");
                    return roleRepository.save(r);
                });

        // 2️⃣ Create default admin user if it doesn't exist // we can also put ADMIN_EMAIL=admin@erp.com
        //ADMIN_PASSWORD=superSecret123 in the application properties and use them instead of hard coding
        if (!authRepository.existsByEmail("admin@erp.com")) {
            User admin = new User();
            admin.setEmail("admin@erp.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(adminRole);
            authRepository.save(admin);

            System.out.println("✅ Default ADMIN user created:");
            System.out.println("   email: admin@erp.com");
            System.out.println("   password: admin123");
        }
    }
}
