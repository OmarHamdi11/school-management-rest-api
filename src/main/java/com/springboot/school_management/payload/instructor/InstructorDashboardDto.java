package com.springboot.school_management.payload.instructor;

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
public class InstructorDashboardDto {
    private Integer totalCourses;
    private Integer totalStudents;
    private Integer totalReviews;
    private Double averageRating;

    private List<CourseDto> recentCourses;

    private List<ReviewDto> recentReviews;
}
