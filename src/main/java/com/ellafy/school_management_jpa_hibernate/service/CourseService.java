package com.ellafy.school_management_jpa_hibernate.service;

import com.ellafy.school_management_jpa_hibernate.payload.CourseDto;
import com.ellafy.school_management_jpa_hibernate.payload.CreateCourseRequest;
import com.ellafy.school_management_jpa_hibernate.response.PageResponse;

public interface CourseService {

    PageResponse<CourseDto> getAllCourses(int pageNo, int pageSize, String sortBy, String sortDir);

    CourseDto getCourseById(Long id);

    CourseDto createCourse(CreateCourseRequest request, Long instructorId);

}
