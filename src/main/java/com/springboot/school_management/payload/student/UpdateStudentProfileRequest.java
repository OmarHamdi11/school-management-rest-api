package com.springboot.school_management.payload.student;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStudentProfileRequest {

    @Size(min = 3, max = 100, message = "Major must be between 3 and 100 character")
    private String major;
}
