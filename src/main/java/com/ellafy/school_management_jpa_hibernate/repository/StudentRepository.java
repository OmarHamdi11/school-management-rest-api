package com.ellafy.school_management_jpa_hibernate.repository;

import com.ellafy.school_management_jpa_hibernate.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student,Long> {

    Optional<Student> findByUsername(String username);
    
}
