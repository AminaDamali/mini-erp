package com.merp.authservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Entity
@NoArgsConstructor // required by jpa
public class User {
    private String email;
    private String password;

    @Id
    @GeneratedValue
    private Long id;


 // or LAZY if you prefer
    @JoinColumn(name = "role_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Role role;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
    public String getEmail() {
        return email;
        }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
}
