package com.springboot.school_management.payload.instructor;

import com.springboot.school_management.payload.CourseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InstructorStatisticsDto {
    private Integer totalCourses;
    private Integer totalStudents;
    private Integer totalReviews;
    private Double averageRating;

    private Map<Integer,Long> ratingDistribution;
    private CourseDto mostPopularCourse;
    private CourseDto highestRatedCourse;
}
