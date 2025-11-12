package com.springboot.school_management.service;

import com.springboot.school_management.payload.student.StudentDto;
import com.springboot.school_management.response.PageResponse;

public interface StudentService {
    // ========== Public/Admin Methods ==========

    PageResponse<StudentDto> getAllStudents(int pageNo, int pageSize, String sortBy, String sortDir);
//
//    /**
//     * جلب طالب بالـ ID
//     */
//    StudentDto getStudentById(Long studentId);
//
//    // ========== Student Profile Methods ==========
//
//    /**
//     * جلب البروفايل
//     */
//    StudentProfileDto getProfile(Long studentId);
//
//    /**
//     * تحديث البروفايل
//     */
//    StudentProfileDto updateProfile(Long studentId, UpdateStudentProfileRequest request);
//
//    // ========== Dashboard & Statistics ==========
//
//    /**
//     * لوحة التحكم
//     */
//    StudentDashboardDto getDashboard(Long studentId);
//
//    /**
//     * الإحصائيات التفصيلية
//     */
//    StudentStatisticsDto getStatistics(Long studentId);
}
