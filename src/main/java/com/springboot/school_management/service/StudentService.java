package com.springboot.school_management.service;

import com.springboot.school_management.payload.student.*;
import com.springboot.school_management.response.PageResponse;

public interface StudentService {
    // ========== Public/Admin Methods ==========

    PageResponse<StudentDto> getAllStudents(int pageNo, int pageSize, String sortBy, String sortDir);

    StudentDto getStudentById(Long studentId);
//
//    // ========== Student Profile Methods ==========

    StudentProfileDto getProfile(Long studentId);

    StudentProfileDto updateProfile(Long studentId, UpdateStudentProfileRequest request);

    // ========== Dashboard & Statistics ==========

    StudentDashboardDto getDashboard(Long studentId);

    StudentStatisticsDto getStatistics(Long studentId);
}
