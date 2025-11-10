package com.springboot.school_management.payload.instructor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InstructorProfileDto {
    private Long id;
    private String username;
    private String specialization;
}
