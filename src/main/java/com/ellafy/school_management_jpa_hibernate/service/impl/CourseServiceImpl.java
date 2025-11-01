package com.ellafy.school_management_jpa_hibernate.service.impl;

import com.ellafy.school_management_jpa_hibernate.entity.Course;
import com.ellafy.school_management_jpa_hibernate.entity.Instructor;
import com.ellafy.school_management_jpa_hibernate.exception.ResourceNotFoundException;
import com.ellafy.school_management_jpa_hibernate.payload.CourseDto;
import com.ellafy.school_management_jpa_hibernate.payload.CreateCourseRequest;
import com.ellafy.school_management_jpa_hibernate.repository.CourseRepository;
import com.ellafy.school_management_jpa_hibernate.repository.InstructorRepository;
import com.ellafy.school_management_jpa_hibernate.repository.StudentRepository;
import com.ellafy.school_management_jpa_hibernate.response.PageResponse;
import com.ellafy.school_management_jpa_hibernate.service.CourseService;
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
