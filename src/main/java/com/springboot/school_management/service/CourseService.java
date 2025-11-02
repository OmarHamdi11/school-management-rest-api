package com.springboot.school_management.service;

import com.springboot.school_management.payload.CourseDto;
import com.springboot.school_management.payload.CreateCourseRequest;
import com.springboot.school_management.response.PageResponse;

public interface CourseService {

    PageResponse<CourseDto> getAllCourses(int pageNo, int pageSize, String sortBy, String sortDir);

    CourseDto getCourseById(Long id);

    CourseDto createCourse(CreateCourseRequest request, Long instructorId);

    CourseDto updateCourse(Long courseId, Long instructorId, CreateCourseRequest request);

}
