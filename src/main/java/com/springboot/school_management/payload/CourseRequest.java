package com.springboot.school_management.payload;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CourseRequest {
    @NotBlank(message = "Course name is required")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    private String name;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @Positive(message = "Price must be positive")
    private Double price;

    @Positive(message = "Duration must be positive")
    private Integer duration;

    @Pattern(regexp = "BEGINNER|INTERMEDIATE|ADVANCED",
            message = "Level must be BEGINNER, INTERMEDIATE, or ADVANCED")
    private String level;
}
