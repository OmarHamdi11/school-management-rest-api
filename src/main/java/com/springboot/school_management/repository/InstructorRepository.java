package com.springboot.school_management.repository;

import com.springboot.school_management.entity.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InstructorRepository extends JpaRepository<Instructor,Long> {

    Optional<Instructor> findByUsername(String username);

    boolean existsByUsername(String username);

    List<Instructor> findBySpecializationContainingIgnoreCase(String specialization);

}
