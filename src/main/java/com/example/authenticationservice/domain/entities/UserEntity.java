package com.example.authenticationservice.domain.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table( name = "user" ,indexes = {
        @Index(name = "username", columnList = "username")
})
public class UserEntity {
    @Id
    private Long id;
    private String username;
    private String password;
    private String role;
}
