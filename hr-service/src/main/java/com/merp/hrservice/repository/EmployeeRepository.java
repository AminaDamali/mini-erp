package com.merp.hrservice.repository;

import com.merp.hrservice.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.beans.JavaBean;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Employee findByEmail(String email);
}
