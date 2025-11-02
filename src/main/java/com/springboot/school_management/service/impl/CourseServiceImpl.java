package com.springboot.school_management.service.impl;

import com.springboot.school_management.entity.Course;
import com.springboot.school_management.entity.Instructor;
import com.springboot.school_management.entity.Student;
import com.springboot.school_management.exception.ResourceNotFoundException;
import com.springboot.school_management.payload.CourseDto;
import com.springboot.school_management.payload.CourseRequest;
import com.springboot.school_management.repository.CourseRepository;
import com.springboot.school_management.repository.InstructorRepository;
import com.springboot.school_management.repository.StudentRepository;
import com.springboot.school_management.response.PageResponse;
import com.springboot.school_management.service.CourseService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;


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


    // ============== Public Functions ==============

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


    // ============== Instructor Functions ==============

    @Override
    @Transactional
    public CourseDto createCourse(CourseRequest request, Long instructorId) {
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

    @Override
    @Transactional
    public CourseDto updateCourse(Long courseId, Long instructorId, CourseRequest request) {

        Course course = courseRepository.findById(courseId).orElseThrow(
                () -> new ResourceNotFoundException("Course", "id", courseId)
        );

        if (!course.getInstructor().getId().equals(instructorId)){
            throw new AccessDeniedException("You are not authorized to update this course");
        }

        course.setName(request.getName());
        course.setDescription(request.getDescription());
        course.setPrice(request.getPrice());
        course.setDuration(request.getDuration());
        course.setLevel(request.getLevel());

        Course updatedCourse = courseRepository.save(course);

        return mapToDto(updatedCourse);
    }

    @Override
    @Transactional
    public CourseDto patchCourse(Long courseId, Long instructorId, CourseRequest request) {

        Course course = courseRepository.findById(courseId).orElseThrow(
                () -> new ResourceNotFoundException("Course", "id", courseId)
        );

        if (!course.getInstructor().getId().equals(instructorId)){
            throw new AccessDeniedException("You are not authorized to update this course");
        }

        modelMapper.getConfiguration().setSkipNullEnabled(true);
        modelMapper.map(request,course);

        Course updatedCourse = courseRepository.save(course);

        return mapToDto(updatedCourse);
    }

    @Override
    @Transactional
    public void deleteCourse(Long courseId, Long instructorId) {

        Course course = courseRepository.findById(courseId).orElseThrow(
                () -> new ResourceNotFoundException("Course", "id", courseId)
        );

        if (!course.getInstructor().getId().equals(instructorId)){
            throw new AccessDeniedException("You are not authorized to update this course");
        }

        courseRepository.deleteById(courseId);


    }

    @Override
    public List<CourseDto> getMyCourses(Long instructorId) {

        List<Course> courses = courseRepository.findByInstructorId(instructorId);

        return courses.stream().map(this::mapToDto).toList();
    }


    // ============== Student Functions ==============

    @Override
    @Transactional
    public void enrollInCourse(Long courseId, Long studentId) {
        Course course = courseRepository.findById(courseId).orElseThrow(
                () -> new ResourceNotFoundException("Course", "id", courseId)
        );

        Student student = studentRepository.findById(studentId).orElseThrow(
                () -> new ResourceNotFoundException("Student", "id", studentId)
        );

        if (student.getEnrolledCourses().contains(course)){
            throw new RuntimeException("You are already enrolled in this course");
        }

        student.enrollInCourse(course);
        studentRepository.save(student);

    }

    @Override
    @Transactional
    public void unenrollFromCourse(Long courseId, Long studentId) {
        Course course = courseRepository.findById(courseId).orElseThrow(
                () -> new ResourceNotFoundException("Course", "id", courseId)
        );

        Student student = studentRepository.findById(studentId).orElseThrow(
                () -> new ResourceNotFoundException("Student", "id", studentId)
        );

        if (!student.getEnrolledCourses().contains(course)){
            throw new RuntimeException("You are not enrolled in this course");
        }

        student.unenrollFromCourse(course);
        studentRepository.save(student);
    }


    private CourseDto mapToDto(Course course) {
        CourseDto dto = modelMapper.map(course, CourseDto.class);

        dto.setInstructorId(course.getInstructor().getId());
        dto.setInstructorName(course.getInstructor().getUsername());
        dto.setEnrolledStudentsCount(course.getEnrolledStudents().size());

        return dto;
    }
}
