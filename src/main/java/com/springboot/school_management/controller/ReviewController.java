package com.springboot.school_management.controller;

import com.springboot.school_management.payload.ReviewCreateRequest;
import com.springboot.school_management.payload.ReviewDto;
import com.springboot.school_management.payload.ReviewUpdateRequest;
import com.springboot.school_management.response.ApiResponse;
import com.springboot.school_management.service.ReviewService;
import com.springboot.school_management.utils.SecurityUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final SecurityUtils securityUtils;

    @Autowired
    public ReviewController(ReviewService reviewService, SecurityUtils securityUtils) {
        this.reviewService = reviewService;
        this.securityUtils = securityUtils;
    }

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping
    public ResponseEntity<ApiResponse<ReviewDto>> createReview(
            @Valid @RequestBody ReviewCreateRequest request
    ){
        Long studentId = securityUtils.getCurrentUserId();
        ReviewDto reviewDto = reviewService.createReview(request, studentId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("Course reviewed Successfully", reviewDto));

    }

    @PreAuthorize("hasRole('STUDENT')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ReviewDto>> updateReview(
            @PathVariable(name = "id") Long reviewId,
            @Valid @RequestBody ReviewUpdateRequest request
    ){
        Long studentId = securityUtils.getCurrentUserId();
        ReviewDto reviewDto = reviewService.updateReview(reviewId, request, studentId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("Review updated Successfully", reviewDto));
    }

    @PreAuthorize("hasRole('STUDENT')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteReview(@PathVariable(name = "id") Long reviewId){
        Long studentId = securityUtils.getCurrentUserId();
        reviewService.deleteReview(reviewId, studentId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("Review deleted Successfully", null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ReviewDto>> getReviewById(@PathVariable(name = "id") Long reviewId){
        ReviewDto reviewDto = reviewService.getReviewById(reviewId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("Review fetched Successfully", reviewDto));
    }



}
