package com.springboot.school_management.controller;

import com.springboot.school_management.payload.ReviewCreateRequest;
import com.springboot.school_management.payload.ReviewDto;
import com.springboot.school_management.payload.ReviewUpdateRequest;
import com.springboot.school_management.response.ApiResponse;
import com.springboot.school_management.response.PageResponse;
import com.springboot.school_management.service.ReviewService;
import com.springboot.school_management.utils.AppConstants;
import com.springboot.school_management.utils.SecurityUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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


    // ============== Public Endpoints ==============

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ReviewDto>> getReviewById(@PathVariable(name = "id") Long reviewId){
        ReviewDto reviewDto = reviewService.getReviewById(reviewId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("Review fetched Successfully", reviewDto));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<ApiResponse<PageResponse<ReviewDto>>> getCourseReviews(
            @PathVariable(name = "courseId") Long courseId,
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION,required = false) String sortDir
    ){
        PageResponse<ReviewDto> response = reviewService.getCourseReviews(courseId, pageNo, pageSize, sortBy, sortDir);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("Reviews fetched Successfully", response));
    }



    // ============== Student Endpoints ==============

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

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/my-reviews")
    public ResponseEntity<ApiResponse<List<ReviewDto>>> getStudentReviews(){
        Long studentId = securityUtils.getCurrentUserId();
        List<ReviewDto> response = reviewService.getStudentReviews(studentId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("Student reviews fetched Successfully", response));
    }


}
