package com.backend.platformDuoc.models;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(nullable = false, length = 25)
    private String username;

    @Column(nullable = false, length = 25)
    private String password;

    @Column(nullable = false, length = 50, unique = true)
    private String email;

    @Column(nullable = false, length = 50)
    private String name;
    
    @Column(nullable = false, length = 50)
    private String lastname;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    @JsonBackReference
    private Rol role;

}