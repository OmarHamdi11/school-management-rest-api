package com.springboot.school_management.service;

import com.springboot.school_management.payload.instructor.InstructorDto;
import com.springboot.school_management.response.PageResponse;

public interface InstructorService {
    // ========== Public Methods ==========

    PageResponse<InstructorDto> getAllInstructors(int pageNo, int pageSize, String sortBy, String sortDir);

//    /**
//     * جلب مدرس بالـ ID
//     */
//    InstructorDto getInstructorById(Long instructorId);
//
//    /**
//     * جلب كورسات مدرس معين
//     */
//    List<CourseDto> getInstructorCourses(Long instructorId);
//
//    /**
//     * البحث عن مدرسين بالتخصص
//     */
//    List<InstructorDto> searchInstructors(String specialization);
//
//    // ========== Instructor Profile Methods ==========
//
//    /**
//     * جلب البروفايل
//     */
//    InstructorProfileDto getProfile(Long instructorId);
//
//    /**
//     * تحديث البروفايل
//     */
//    InstructorProfileDto updateProfile(Long instructorId, UpdateInstructorProfileRequest request);
//
//    // ========== Dashboard & Statistics ==========
//
//    /**
//     * لوحة التحكم
//     */
//    InstructorDashboardDto getDashboard(Long instructorId);
//
//    /**
//     * الإحصائيات التفصيلية
//     */
//    InstructorStatisticsDto getStatistics(Long instructorId);
}
