package com.springboot.school_management.service;

import com.springboot.school_management.payload.CourseDto;
import com.springboot.school_management.payload.CourseRequest;
import com.springboot.school_management.response.PageResponse;

import java.util.Map;

public interface CourseService {

    PageResponse<CourseDto> getAllCourses(int pageNo, int pageSize, String sortBy, String sortDir);

    CourseDto getCourseById(Long id);

    CourseDto createCourse(CourseRequest request, Long instructorId);

    CourseDto updateCourse(Long courseId, Long instructorId, CourseRequest request);

    CourseDto patchCourse(Long courseId, Long instructorId, CourseRequest request);

}
