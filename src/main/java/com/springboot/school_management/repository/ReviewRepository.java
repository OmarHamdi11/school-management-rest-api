package com.springboot.school_management.repository;

import com.springboot.school_management.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review,Long> {

    Page<Review> findByCourseId(Long courseId, Pageable pageable);

    List<Review> findByStudentId(Long studentId);

    List<Review> findByCourseId(Long courseId);

    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);

    Optional<Review> findByStudentIdAndCourseId(Long studentId, Long courseId);

    Page<Review> findByCourseIdOrderByRatingDesc(Long courseId, Pageable pageable);

    Page<Review> findByCourseIdOrderByCreatedAtDesc(Long courseId, Pageable pageable);

    @Query("select AVG(r.rating) from Review r where r.course.id = :courseId")
    Double findByAverageRatingByCourseId(Long courseId);

    Long countByCourseId(Long courseId);
}
