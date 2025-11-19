package com.merp.authservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // specify strategy
    private Long id;

    @Column(unique = true, nullable = false)
    private String name; // ADMIN, HR_MANAGER, EMPLOYEE

    private String description;
}
