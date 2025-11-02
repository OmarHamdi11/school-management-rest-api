package com.springboot.school_management.controller;


import com.springboot.school_management.payload.CourseDto;
import com.springboot.school_management.payload.CourseRequest;
import com.springboot.school_management.response.ApiResponse;
import com.springboot.school_management.response.PageResponse;
import com.springboot.school_management.service.CourseService;
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
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;
    private final SecurityUtils securityUtils;

    @Autowired
    public CourseController(CourseService courseService, SecurityUtils securityUtils) {
        this.courseService = courseService;
        this.securityUtils = securityUtils;
    }




    // ============== Public Endpoints ==============

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




    // ============== Instructor Endpoints ==============

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @PostMapping
    public ResponseEntity<ApiResponse<CourseDto>> createCourse(@Valid @RequestBody CourseRequest request){

        Long instructorId = securityUtils.getCurrentUserId();

        CourseDto courseDto =courseService.createCourse(request,instructorId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.created("Course Created Successfully",courseDto));

    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @PutMapping("{id}")
    public ResponseEntity<ApiResponse<CourseDto>> updateCourse(
            @PathVariable(name = "id") Long courseId,
            @Valid @RequestBody CourseRequest request
    ){
        Long instructorId = securityUtils.getCurrentUserId();
        CourseDto updatedCourse = courseService.updateCourse(courseId, instructorId, request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("Course Updated Successfully", updatedCourse));

    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @PatchMapping("{id}")
    public ResponseEntity<ApiResponse<CourseDto>> patchCourse(
            @PathVariable(name = "id") Long courseId,
            @RequestBody CourseRequest request
    ){
        Long instructorId = securityUtils.getCurrentUserId();
        CourseDto updatedCourse = courseService.patchCourse(courseId, instructorId, request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("Course Updated Successfully", updatedCourse));

    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<String>> deleteCourse(
            @PathVariable(name = "id") Long courseId
    ){
        Long instructorId = securityUtils.getCurrentUserId();
        courseService.deleteCourse(courseId,instructorId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("Course Deleted Successfully", null));

    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @GetMapping("/my-courses")
    public ResponseEntity<ApiResponse<List<CourseDto>>> getMyCourses(){
        Long instructorId = securityUtils.getCurrentUserId();
        List<CourseDto> instructorCourses = courseService.getInstructorCourses(instructorId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("Courses retrieved successfully", instructorCourses));

    }

    // GET /api/courses/{id}/students
//    @PreAuthorize("hasRole('INSTRUCTOR')")
//    @GetMapping("/{courseId}/students")
//    public ResponseEntity<ApiResponse<List<StudentDto>>> getCourseStudents(@PathVariable Long courseId)


    // ============== Student Endpoints ==============

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/{id}/enroll")
    public ResponseEntity<ApiResponse<String>> enrollInCourse(@PathVariable(name = "id") Long courseId){
        Long studentId = securityUtils.getCurrentUserId();
        courseService.enrollInCourse(courseId, studentId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("Successfully enrolled in the course", null));
    }


    @PreAuthorize("hasRole('STUDENT')")
    @DeleteMapping("/{id}/unenroll")
    public ResponseEntity<ApiResponse<String>> unenrollFromCourse(@PathVariable(name = "id") Long courseId){
        Long studentId = securityUtils.getCurrentUserId();
        courseService.unenrollFromCourse(courseId, studentId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("Successfully unenrolled from the course", null));
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/my-enrollments")
    public ResponseEntity<ApiResponse<List<CourseDto>>> getStudentEnrollments(){
        Long studentId = securityUtils.getCurrentUserId();
        List<CourseDto> studentEnrollments = courseService.getStudentEnrollments(studentId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("Enrollments retrieved successfully", studentEnrollments));
    }


    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/{id}/check-enrollment")
    public ResponseEntity<ApiResponse<String>> checkEnrollmentStatus(@PathVariable(name = "id") Long courseId){
        Long studentId = securityUtils.getCurrentUserId();
        String status = courseService.isStudentEnrolled(courseId, studentId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("Enrollments status retrieved", status));
    }


}
