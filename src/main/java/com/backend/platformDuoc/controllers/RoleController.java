package com.backend.platformDuoc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.platformDuoc.models.Rol;
import com.backend.platformDuoc.services.RoleService;

@RestController
@RequestMapping("/api/v1")
public class RoleController {
    
    @Autowired
    private RoleService roleService;

    @PostMapping("/role/new")
    public ResponseEntity<?> guardarRol(@RequestBody Rol rol){
        Rol rolGuardado = roleService.save(rol);
        return ResponseEntity.status(201).body(rolGuardado);
    }
}
