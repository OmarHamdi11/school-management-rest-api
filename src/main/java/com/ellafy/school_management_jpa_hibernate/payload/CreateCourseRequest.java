package com.ellafy.school_management_jpa_hibernate.payload;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCourseRequest {
    private String name;
    private String description;
    private Double price;
    private Integer duration;
    private String level;
}
