package com.merp.hrservice.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ Link to Auth-Service user ID

    private Long userId;

    private String firstName;
    private String lastName;

    @Column(unique = true)
    private String email;

    private String phone;
    private String department;
    private String jobTitle;

    private LocalDate hireDate;
    private Double salary;
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "contract_type", columnDefinition = "VARCHAR(20)")
    private ContractType contractType;
    private String city;

    @Enumerated(EnumType.STRING)
    private EmployeeStatus status = EmployeeStatus.ACTIVE;

    private LocalDate createdAt = LocalDate.now();
    private LocalDate updatedAt = LocalDate.now();
}
