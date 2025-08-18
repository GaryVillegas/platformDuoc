package com.backend.platformDuoc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.backend.platformDuoc.dto.ActivityDTO;
import com.backend.platformDuoc.models.Activity;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Integer>{

    //Nueva consulta DTO
    @Query("SELECT new com.backend.platformDuoc.dto.ActivityDTO(a) " + "FROM Activity a")
    List<ActivityDTO> findActivityDTO();
}