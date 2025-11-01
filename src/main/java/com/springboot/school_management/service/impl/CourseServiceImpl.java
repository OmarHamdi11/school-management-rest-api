package com.springboot.school_management.service.impl;

import com.springboot.school_management.entity.Course;
import com.springboot.school_management.entity.Instructor;
import com.springboot.school_management.exception.ResourceNotFoundException;
import com.springboot.school_management.payload.CourseDto;
import com.springboot.school_management.payload.CreateCourseRequest;
import com.springboot.school_management.repository.CourseRepository;
import com.springboot.school_management.repository.InstructorRepository;
import com.springboot.school_management.repository.StudentRepository;
import com.springboot.school_management.response.PageResponse;
import com.springboot.school_management.service.CourseService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final InstructorRepository instructorRepository;
    private final StudentRepository studentRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository,
                             InstructorRepository instructorRepository,
                             StudentRepository studentRepository,
                             ModelMapper modelMapper) {
        this.courseRepository = courseRepository;
        this.instructorRepository = instructorRepository;
        this.studentRepository = studentRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public PageResponse<CourseDto> getAllCourses(int pageNo, int pageSize, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        // create pageable instance
        Pageable pageable = PageRequest.of(pageNo,pageSize, sort);

        Page<Course> coursePage = courseRepository.findAll(pageable);

        Page<CourseDto> courseDtoPage = coursePage.map(this::mapToDto);

        return  new PageResponse<>(courseDtoPage);

    }

    @Override
    public CourseDto getCourseById(Long id) {

        Course course = courseRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Course", "Id", id)
        );

        return mapToDto(course);
    }


    @Override
    public CourseDto createCourse(CreateCourseRequest request, Long instructorId) {
        Instructor instructor = instructorRepository.findById(instructorId).orElseThrow(
                () -> new RuntimeException("Instructor Not Found")
        );

        Course course = new Course();
        course.setName(request.getName());
        course.setDescription(request.getDescription());
        course.setPrice(request.getPrice());
        course.setDuration(request.getDuration());
        course.setLevel(request.getLevel());
        course.setInstructor(instructor);

        Course savedCourse = courseRepository.save(course);

        return mapToDto(savedCourse);
    }


    // ✅ الـ mapping بقى بسيط جداً
    private CourseDto mapToDto(Course course) {
        CourseDto dto = modelMapper.map(course, CourseDto.class);

        // Custom mappings للحاجات اللي مش automatic
        dto.setInstructorId(course.getInstructor().getId());
        dto.setInstructorName(course.getInstructor().getUsername());
        dto.setEnrolledStudentsCount(course.getEnrolledStudents().size());

        return dto;
    }
}
