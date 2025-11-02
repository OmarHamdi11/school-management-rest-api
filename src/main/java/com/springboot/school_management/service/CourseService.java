package com.springboot.school_management.service;

import com.springboot.school_management.payload.CourseDto;
import com.springboot.school_management.payload.CourseRequest;
import com.springboot.school_management.response.PageResponse;

import java.util.List;

public interface CourseService {

    // ============== Public Functions ==============

    PageResponse<CourseDto> getAllCourses(int pageNo, int pageSize, String sortBy, String sortDir);

    CourseDto getCourseById(Long id);


    // ============== Instructor Functions ==============

    CourseDto createCourse(CourseRequest request, Long instructorId);

    CourseDto updateCourse(Long courseId, Long instructorId, CourseRequest request);

    CourseDto patchCourse(Long courseId, Long instructorId, CourseRequest request);

    void deleteCourse(Long courseId, Long instructorId);

    List<CourseDto> getInstructorCourses(Long instructorId);


    // ============== Student Functions ==============

    void enrollInCourse(Long courseId, Long studentId);

    void unenrollFromCourse(Long courseId, Long studentId);

    List<CourseDto> getStudentEnrollments(Long studentId);

    String isStudentEnrolled(Long courseId, Long studentId);

}
