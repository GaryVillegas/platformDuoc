package com.backend.platformDuoc.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.backend.platformDuoc.dto.UserDTO;
import com.backend.platformDuoc.models.User;
import com.backend.platformDuoc.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserService{
    
    final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public List<User> findAllUser(){
        return userRepository.findAll();
    }

    public User findByUserName(String username){
        return userRepository.findUserByUsername(username);
    }

    //Devuelve usuario con rol especifico
    public List<UserDTO> getStudentRole(){
        return userRepository.findUserByRoleId(4);
    }

    public List<UserDTO> getExecutiveRole(){
        return userRepository.findUserByRoleId(3);
    }

}
