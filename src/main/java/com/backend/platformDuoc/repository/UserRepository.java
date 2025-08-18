package com.backend.platformDuoc.repository;

import org.springframework.stereotype.Repository;

import com.backend.platformDuoc.models.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
    User findByEmail(String email);

    User findUserByUsername(String username);

    boolean existsByRole_Id(Integer id);

    List<User> findByRole_id(Integer roleId);
}
