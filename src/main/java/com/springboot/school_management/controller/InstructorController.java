package com.springboot.school_management.controller;

import com.springboot.school_management.payload.instructor.InstructorDto;
import com.springboot.school_management.response.ApiResponse;
import com.springboot.school_management.response.PageResponse;
import com.springboot.school_management.service.InstructorService;
import com.springboot.school_management.utils.AppConstants;
import com.springboot.school_management.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
                .body(ApiResponse.success("Instructors fetched Successfully", response));
    }
}
