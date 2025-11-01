package com.ellafy.school_management_jpa_hibernate.repository;

import com.ellafy.school_management_jpa_hibernate.entity.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InstructorRepository extends JpaRepository<Instructor,Long> {

    Optional<Instructor> findByUsername(String username);

    List<Instructor> findBySpecialization(String specialization);

}
