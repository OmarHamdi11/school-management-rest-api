package com.springboot.school_management.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {
    private Long id;
    private int rating;
    private String comment;

    private Long studentId;
    private String studentName;

    private Long courseId;
    private String courseName;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
