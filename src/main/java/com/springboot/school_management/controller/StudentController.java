package com.springboot.school_management.controller;

import com.springboot.school_management.payload.student.*;
import com.springboot.school_management.response.ApiResponse;
import com.springboot.school_management.response.PageResponse;
import com.springboot.school_management.service.StudentService;
import com.springboot.school_management.utils.AppConstants;
import com.springboot.school_management.utils.SecurityUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;
    private final SecurityUtils securityUtils;

    @Autowired
    public StudentController(StudentService studentService, SecurityUtils securityUtils) {
        this.studentService = studentService;
        this.securityUtils = securityUtils;
    }


    // ============== Public Endpoints ==============

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<StudentDto>>> getAllStudents(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ){
        PageResponse<StudentDto> response = studentService.getAllStudents(pageNo, pageSize, sortBy, sortDir);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("Students retrieved successfully", response));
    }

    @GetMapping("{studentId}")
    public ResponseEntity<ApiResponse<StudentDto>> getStudent(
            @PathVariable(name = "studentId") Long studentId
    ){
        StudentDto response = studentService.getStudentById(studentId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("Student found", response));
    }



    // ============== Student Endpoints ==============

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<StudentProfileDto>> getStudentProfile(){
        Long studentId = securityUtils.getCurrentUserId();
        StudentProfileDto response = studentService.getProfile(studentId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("Profile retrieved successfully", response));
    }

    @PreAuthorize("hasRole('STUDENT')")
    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<StudentProfileDto>> updateStudentProfile(
        @Valid @RequestBody UpdateStudentProfileRequest request
    ){
        Long studentId = securityUtils.getCurrentUserId();
        StudentProfileDto response = studentService.updateProfile(studentId,request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("Profile updated successfully", response));
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<StudentDashboardDto>> getDashboard(){
        Long studentId = securityUtils.getCurrentUserId();
        StudentDashboardDto response = studentService.getDashboard(studentId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("Profile data retrieved successfully", response));
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/statistics")
    public ResponseEntity<ApiResponse<StudentStatisticsDto>> getStatistics(){
        Long studentId = securityUtils.getCurrentUserId();
        StudentStatisticsDto response = studentService.getStatistics(studentId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("Statistics retrieved successfully", response));
    }

}
