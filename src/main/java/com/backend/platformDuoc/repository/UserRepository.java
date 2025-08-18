package com.backend.platformDuoc.repository;

import org.springframework.stereotype.Repository;

import com.backend.platformDuoc.dto.UserDTO;
import com.backend.platformDuoc.models.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
    User findByEmail(String email);

    User findUserByUsername(String username);

    boolean existsByRole_Id(Integer id);

    List<User> findByRole_id(Integer roleId);

    //nueva consulta para DTO
    @Query("SELECT new com.backend.platformDuoc.dto.UserDTO(u.id, u.name, u.lastname) " +
           "FROM User u WHERE u.role.id = :roleId")
    List<UserDTO> findUserByRoleId(@Param("roleId") Integer roleId);
}
