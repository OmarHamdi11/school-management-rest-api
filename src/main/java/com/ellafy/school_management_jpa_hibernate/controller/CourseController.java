package com.ellafy.school_management_jpa_hibernate.controller;


import com.ellafy.school_management_jpa_hibernate.payload.CourseDto;
import com.ellafy.school_management_jpa_hibernate.payload.CreateCourseRequest;
import com.ellafy.school_management_jpa_hibernate.response.ApiResponse;
import com.ellafy.school_management_jpa_hibernate.response.PageResponse;
import com.ellafy.school_management_jpa_hibernate.service.CourseService;
import com.ellafy.school_management_jpa_hibernate.utils.AppConstants;
import com.ellafy.school_management_jpa_hibernate.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;
    private final SecurityUtils securityUtils;

    @Autowired
    public CourseController(CourseService courseService, SecurityUtils securityUtils) {
        this.courseService = courseService;
        this.securityUtils = securityUtils;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<CourseDto>>> getAllCourses(
            @RequestParam(value = "pageNo" , defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize" , defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ){
        PageResponse<CourseDto> pageResponse = courseService.getAllCourses(pageNo,pageSize,sortBy,sortDir);
        return ResponseEntity.ok(ApiResponse.success("Courses retrieved successfully", pageResponse));
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<CourseDto>> getCourseById(@PathVariable(name = "id") Long id){

        CourseDto courseDto = courseService.getCourseById(id);
        ApiResponse<CourseDto> response = ApiResponse.success("Course Found", courseDto);

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @PostMapping
    public ResponseEntity<ApiResponse<CourseDto>> createCourse(@RequestBody CreateCourseRequest request){

        Long instructorId = securityUtils.getCurrentUserId();

        CourseDto courseDto =courseService.createCourse(request,instructorId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.created("Course Created Successfully",courseDto));

    }


}
