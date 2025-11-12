package com.springboot.school_management.payload.student;

import com.springboot.school_management.payload.instructor.InstructorDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentStatisticsDto {
    private int totalEnrollments;
    private int totalReviews;
    private Double averageRating;
    private Map<Integer,Long> givenRatingDistribution;
    private String mostEnrolledLevel;
    private InstructorDto favouriteInstructor;
}
