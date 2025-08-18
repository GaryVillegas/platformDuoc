package com.backend.platformDuoc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.platformDuoc.services.RoleService;
import com.backend.platformDuoc.services.UserService;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    
    @Autowired
    private UserService userService;

    @Autowired RoleService roleService;

    public UserController(UserService userService, RoleService roleService){
        this.userService = userService;
        this.roleService = roleService;
    }

    @PostMapping("/users")
    public ResponseEntity<?> listUser(){
        if(userService.findAllUser().isEmpty()){
            return ResponseEntity.status(404).body("No se encontraron usuarios.");
        }

        return ResponseEntity.ok().body(userService.findAllUser());
    }

    @PostMapping("/student")
    public ResponseEntity<?> listStudent(){
        if(userService.getStudentRole().isEmpty()){
            return ResponseEntity.status(404).body("No se encontraron usuarios estudiantes");
        }

        return ResponseEntity.ok().body(userService.getStudentRole());
    }
}
