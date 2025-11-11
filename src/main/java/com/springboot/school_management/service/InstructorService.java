package com.springboot.school_management.service;

import com.springboot.school_management.payload.CourseDto;
import com.springboot.school_management.payload.instructor.*;
import com.springboot.school_management.response.PageResponse;

import java.util.List;

public interface InstructorService {
    // ========== Public Methods ==========

    PageResponse<InstructorDto> getAllInstructors(int pageNo, int pageSize, String sortBy, String sortDir);

    InstructorDto getInstructorById(Long instructorId);

    List<CourseDto> getInstructorCourses(Long instructorId);

    List<InstructorDto> searchInstructors(String specialization);

    // ========== Instructor Profile Methods ==========

    InstructorProfileDto getProfile(Long instructorId);

    InstructorProfileDto updateProfile(Long instructorId, UpdateInstructorProfileRequest request);

    // ========== Dashboard & Statistics ==========

    InstructorDashboardDto getDashboard(Long instructorId);

    InstructorStatisticsDto getStatistics(Long instructorId);
}
