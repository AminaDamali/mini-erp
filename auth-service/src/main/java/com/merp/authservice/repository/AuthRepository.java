package com.merp.authservice.repository;

import com.merp.authservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



    @Repository
    public interface AuthRepository extends JpaRepository<User, Long> {
        User findByEmail(String Email);

        boolean existsByEmail(String email);

    }


