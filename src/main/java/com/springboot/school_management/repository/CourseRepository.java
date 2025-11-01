package com.springboot.school_management.repository;

import com.springboot.school_management.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course,Long> {

    List<Course> findByInstructorId(Long instructorId);

    List<Course> findByNameContainingIgnoreCase(String name);

    List<Course> findByLevel(String level);

    int countByInstructorId(Long instructorId);

    @Query("SELECT COUNT(s) FROM Course c JOIN c.enrolledStudents s WHERE c.id = :courseId")
    Long countEnrolledStudents(@Param("courseId") Long courseId);

}
