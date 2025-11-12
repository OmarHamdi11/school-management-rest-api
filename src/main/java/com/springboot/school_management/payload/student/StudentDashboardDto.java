package com.springboot.school_management.payload.student;

import com.springboot.school_management.payload.CourseDto;
import com.springboot.school_management.payload.ReviewDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentDashboardDto {
    private int totalEnrollments;
    private int totalReviews;
    private Double averageRating;
    private List<CourseDto> enrolledCourses;
    private List<ReviewDto> recentReviews;
    private List<CourseDto> recommendedCourses;
}
