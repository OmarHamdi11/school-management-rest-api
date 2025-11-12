package com.springboot.school_management.controller;

import com.springboot.school_management.payload.CourseDto;
import com.springboot.school_management.payload.instructor.*;
import com.springboot.school_management.response.ApiResponse;
import com.springboot.school_management.response.PageResponse;
import com.springboot.school_management.service.InstructorService;
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
@RequestMapping("/api/instructors")
public class InstructorController {

    private final InstructorService instructorService;
    private final SecurityUtils securityUtils;

    @Autowired
    public InstructorController(InstructorService instructorService, SecurityUtils securityUtils) {
        this.instructorService = instructorService;
        this.securityUtils = securityUtils;
    }


    // ============== Public Endpoints ==============

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<InstructorDto>>> getAllInstructors(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ){
        PageResponse<InstructorDto> response = instructorService.getAllInstructors(pageNo,pageSize,sortBy,sortDir);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("Instructors retrieved Successfully", response));
    }

    @GetMapping("/{instructorId}")
    public ResponseEntity<ApiResponse<InstructorDto>> getInstructorById(@PathVariable(name = "instructorId") Long instructorId){
        InstructorDto response = instructorService.getInstructorById(instructorId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("Instructor found", response));
    }

    @GetMapping("/{instructorId}/courses")
    public ResponseEntity<ApiResponse<List<CourseDto>>> getInstructorCourses(
            @PathVariable(name = "instructorId") Long instructorId
    ){
      List<CourseDto> response = instructorService.getInstructorCourses(instructorId);

      return ResponseEntity
              .status(HttpStatus.OK)
              .body(ApiResponse.success("Instructor courses retrieved Successfully", response));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<InstructorDto>>> searchInstructors(
            @RequestParam String specialization
    ){
        List<InstructorDto> response = instructorService.searchInstructors(specialization);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("Search results", response));
    }



    // ============== Instructor Endpoints ==============

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<InstructorProfileDto>> getProfile(){
        Long instructorId = securityUtils.getCurrentUserId();
        InstructorProfileDto response = instructorService.getProfile(instructorId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("Profile retrieved successfully", response));
    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<InstructorProfileDto>> updateProfile(
            @Valid @RequestBody UpdateInstructorProfileRequest request
    ){
        Long instructorId = securityUtils.getCurrentUserId();
        InstructorProfileDto response = instructorService.updateProfile(instructorId,request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("Profile updated successfully", response));

    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<InstructorDashboardDto>> getDashboard(){
        Long instructorId = securityUtils.getCurrentUserId();
        InstructorDashboardDto response = instructorService.getDashboard(instructorId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("Dashboard data retrieved successfully", response));
    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @GetMapping("/statistics")
    public ResponseEntity<ApiResponse<InstructorStatisticsDto>> getStatistics(){
        Long instructorId = securityUtils.getCurrentUserId();
        InstructorStatisticsDto response = instructorService.getStatistics(instructorId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("Statistics retrieved successfully", response));
    }

}
