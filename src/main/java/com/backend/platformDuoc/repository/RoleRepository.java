package com.backend.platformDuoc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.platformDuoc.models.Rol;

public interface RoleRepository extends JpaRepository<Rol, Integer> {
    
}
