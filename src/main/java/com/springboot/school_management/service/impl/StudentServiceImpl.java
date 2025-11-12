package com.springboot.school_management.service.impl;

import com.springboot.school_management.entity.Review;
import com.springboot.school_management.entity.Student;
import com.springboot.school_management.payload.student.StudentDto;
import com.springboot.school_management.repository.CourseRepository;
import com.springboot.school_management.repository.InstructorRepository;
import com.springboot.school_management.repository.ReviewRepository;
import com.springboot.school_management.repository.StudentRepository;
import com.springboot.school_management.response.PageResponse;
import com.springboot.school_management.service.StudentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final InstructorRepository instructorRepository;
    private final CourseRepository courseRepository;
    private final ReviewRepository reviewRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository,
                              InstructorRepository instructorRepository,
                              CourseRepository courseRepository,
                              ReviewRepository reviewRepository,
                              ModelMapper modelMapper
    ) {
        this.studentRepository = studentRepository;
        this.instructorRepository = instructorRepository;
        this.courseRepository = courseRepository;
        this.reviewRepository = reviewRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public PageResponse<StudentDto> getAllStudents(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo,pageSize,sort);

        Page<Student> studentsPage = studentRepository.findAll(pageable);

        Page<StudentDto> studentsDtoPage = studentsPage.map(this::mapToDto);

        return new PageResponse<>(studentsDtoPage);
    }

    private StudentDto mapToDto(Student student){
        StudentDto studentDto = modelMapper.map(student,StudentDto.class);
        studentDto.setTotalEnrollments(student.getEnrolledCourses().size());

        List<Review> reviews = reviewRepository.findByStudentId(student.getId());
        studentDto.setTotalReviews(reviews.size());

        double averageRating = reviews.isEmpty()? 0.0 :
                reviews.stream()
                        .mapToInt(Review::getRating)
                        .average()
                        .orElse(0.0);
        studentDto.setAverageRating(averageRating);

        return studentDto;
    }
}
