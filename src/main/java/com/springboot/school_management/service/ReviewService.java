package com.springboot.school_management.service;

import com.springboot.school_management.payload.ReviewCreateRequest;
import com.springboot.school_management.payload.ReviewDto;
import com.springboot.school_management.payload.ReviewUpdateRequest;
import com.springboot.school_management.response.PageResponse;

import java.util.List;

public interface ReviewService {

    // ========== CRUD Operations ==========

    ReviewDto createReview(ReviewCreateRequest request, Long studentId);

    ReviewDto updateReview(Long reviewId, ReviewUpdateRequest request, Long studentId);

    void deleteReview(Long reviewId, Long studentId);

    ReviewDto getReviewById(Long reviewId);

    // ========== Query Operations ==========

    PageResponse<ReviewDto> getCourseReviews(Long courseId, int pageNo, int pageSize, String sortBy, String sortDir);

    List<ReviewDto> getStudentReviews(Long studentId);

    ReviewDto getStudentReviewForCourse(Long studentId, Long courseId);
//
//    // ========== Statistics ==========

    Double getAverageRating(Long courseId);

    Long getReviewsCount(Long courseId);

    boolean hasReviewed(Long studentId, Long courseId);

}
