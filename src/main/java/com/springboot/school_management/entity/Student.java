package com.springboot.school_management.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("STUDENT")
public class Student extends User {
    private String major;

    @ManyToMany
    @JoinTable(
            name = "student_course",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private List<Course> enrolledCourses = new ArrayList<>();

    // Helper method
    public void enrollInCourse(Course course) {
        this.enrolledCourses.add(course);
        course.getEnrolledStudents().add(this);
    }

    public void unenrollFromCourse(Course course) {
        this.enrolledCourses.remove(course);
        course.getEnrolledStudents().remove(this);
    }

}










//
//import jakarta.persistence.*;
//
//import java.util.Set;
//
//@Entity
//@Table(name = "students")
//public class Student {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id")
//    private int id;
//
//    @Column(name = "name")
//    private String name;
//
//    @Column(name = "username", nullable = false, unique = true)
//    private String username;
//
//    @Column(name = "email", nullable = false, unique = true)
//    private String email;
//
//    @Column(name = "password", nullable = false)
//    private String password;
//
//    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
//    @JoinTable(
//            name = "users_roles",
//            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
//            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
//    )
//    private Set<Role> roles;
//
////    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "students",
////            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
////    private List<Course> courses;
//
//
//}
