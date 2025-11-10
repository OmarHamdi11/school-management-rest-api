package com.springboot.school_management.payload.instructor;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateInstructorProfileRequest {

    @Size(min = 3, max = 100, message = "Specialization must be between 3 and 100 characters")
    private String specialization;

}
