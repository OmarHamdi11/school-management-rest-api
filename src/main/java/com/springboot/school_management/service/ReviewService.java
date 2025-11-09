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
//
//    // ========== Query Operations ==========
//
//    /**
//     * جلب مراجعات كورس معين (مع pagination)
//     */
//    PageResponse<ReviewDto> getCourseReviews(Long courseId, int pageNo, int pageSize, String sortBy, String sortDir);
//
//    /**
//     * جلب مراجعات طالب معين
//     */
//    List<ReviewDto> getStudentReviews(Long studentId);
//
//    /**
//     * جلب مراجعة طالب لكورس معين
//     */
//    ReviewDto getStudentReviewForCourse(Long studentId, Long courseId);
//
//    // ========== Statistics ==========
//
//    /**
//     * حساب متوسط التقييم لكورس
//     */
//    Double getAverageRating(Long courseId);
//
//    /**
//     * عدد المراجعات لكورس
//     */
//    Long getReviewsCount(Long courseId);
//
//    /**
//     * التحقق من وجود مراجعة
//     */
//    boolean hasReviewed(Long studentId, Long courseId);

}
