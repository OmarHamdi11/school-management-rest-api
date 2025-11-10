package com.springboot.school_management.service.impl;

import com.springboot.school_management.entity.Course;
import com.springboot.school_management.entity.Instructor;
import com.springboot.school_management.entity.Review;
import com.springboot.school_management.payload.instructor.InstructorDto;
import com.springboot.school_management.repository.CourseRepository;
import com.springboot.school_management.repository.InstructorRepository;
import com.springboot.school_management.repository.ReviewRepository;
import com.springboot.school_management.response.PageResponse;
import com.springboot.school_management.service.InstructorService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InstructorServiceImpl implements InstructorService {

    private final InstructorRepository instructorRepository;
    private final CourseRepository courseRepository;
    private final ReviewRepository reviewRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public InstructorServiceImpl(InstructorRepository instructorRepository, CourseRepository courseRepository, ReviewRepository reviewRepository, ModelMapper modelMapper) {
        this.instructorRepository = instructorRepository;
        this.courseRepository = courseRepository;
        this.reviewRepository = reviewRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public PageResponse<InstructorDto> getAllInstructors(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo,pageSize,sort);

        Page<Instructor> instructorPage = instructorRepository.findAll(pageable);

        Page<InstructorDto> instructorDtoPage = instructorPage.map(this::mapToDto);

        return new PageResponse<>(instructorDtoPage);
    }

    private InstructorDto mapToDto(Instructor instructor){
        InstructorDto instructorDto = new InstructorDto();
        instructorDto = modelMapper.map(instructor, InstructorDto.class);

        List<Course> courses = courseRepository.findByInstructorId(instructor.getId());
        instructorDto.setTotalCourses(courses.size());

        int totalStudents = courses.stream().mapToInt(course -> course.getEnrolledStudents().size()).sum();
        instructorDto.setTotalStudents(totalStudents);

        List<Review> reviews = new ArrayList<>();
        for (Course course: courses){
            reviews.addAll(reviewRepository.findByCourseId(course.getId()));
        }
        double avgRating = reviews.isEmpty()? 0.0
                : reviews.stream().mapToInt(Review::getRating).average().orElse(0.0);
        instructorDto.setAverageRating(Math.round(avgRating * 10.0) / 10.0);

        return instructorDto;
    }
}
