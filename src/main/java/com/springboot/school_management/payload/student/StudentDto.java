package com.springboot.school_management.payload.student;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentDto {
    private Long id;
    private String username;
    private String major;
    private int totalEnrollments;
    private int totalReviews;
    private Double averageRating;
}
