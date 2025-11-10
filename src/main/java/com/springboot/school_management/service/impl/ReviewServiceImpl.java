package com.springboot.school_management.service.impl;

import com.springboot.school_management.entity.Course;
import com.springboot.school_management.entity.Review;
import com.springboot.school_management.entity.Student;
import com.springboot.school_management.exception.ResourceNotFoundException;
import com.springboot.school_management.payload.ReviewCreateRequest;
import com.springboot.school_management.payload.ReviewDto;
import com.springboot.school_management.payload.ReviewUpdateRequest;
import com.springboot.school_management.repository.CourseRepository;
import com.springboot.school_management.repository.ReviewRepository;
import com.springboot.school_management.repository.StudentRepository;
import com.springboot.school_management.response.PageResponse;
import com.springboot.school_management.service.ReviewService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ModelMapper modelMapper;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public ReviewServiceImpl(ReviewRepository reviewRepository,
                             ModelMapper modelMapper,
                             StudentRepository studentRepository,
                             CourseRepository courseRepository) {
        this.reviewRepository = reviewRepository;
        this.modelMapper = modelMapper;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public ReviewDto createReview(ReviewCreateRequest request, Long studentId) {

        Student student = studentRepository.findById(studentId).orElseThrow(
                () -> new ResourceNotFoundException("Student", "id", studentId)
        );

        Long courseId = request.getCourseId();
        Course course = courseRepository.findById(courseId).orElseThrow(
                () -> new ResourceNotFoundException("Course", "id", courseId)
        );

        if (!student.getEnrolledCourses().contains(course)){
            throw new RuntimeException("You must be enrolled in this course to review it");
        }

        if (reviewRepository.existsByStudentIdAndCourseId(studentId,courseId)){
            throw new RuntimeException("You have already reviewed this course");
        }

        Review review = new Review();
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review.setStudent(student);
        review.setCourse(course);

        Review savedReview = reviewRepository.save(review);

        return mapToDto(savedReview);
    }

    @Override
    public ReviewDto updateReview(Long reviewId, ReviewUpdateRequest request, Long studentId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(
                () -> new ResourceNotFoundException("Review", "id", reviewId)
        );

        Student student = studentRepository.findById(studentId).orElseThrow(
                () -> new ResourceNotFoundException("Student", "id", studentId)
        );

        if (!review.getStudent().getId().equals(student.getId())){
            throw new RuntimeException("You are not authorized to update this review");
        }

        review.setRating(request.getRating());
        review.setComment(request.getComment());

        Review updatedReview = reviewRepository.save(review);

        return mapToDto(updatedReview);
    }

    @Override
    public void deleteReview(Long reviewId, Long studentId) {
        Student student = studentRepository.findById(studentId).orElseThrow(
                () -> new ResourceNotFoundException("Student", "id", studentId)
        );

        Review review = reviewRepository.findById(reviewId).orElseThrow(
                () -> new ResourceNotFoundException("Review", "id", reviewId)
        );

        if (!review.getStudent().getId().equals(student.getId())){
            throw new RuntimeException("You are not authorized to delete this review");
        }

        reviewRepository.deleteById(reviewId);

    }

    @Override
    public ReviewDto getReviewById(Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(
                () -> new ResourceNotFoundException("Review", "id", reviewId)
        );

        return mapToDto(review);
    }

    @Override
    public PageResponse<ReviewDto> getCourseReviews(Long courseId, int pageNo, int pageSize, String sortBy, String sortDir) {
        Course course = courseRepository.findById(courseId).orElseThrow(
                () -> new ResourceNotFoundException("Course", "id", courseId)
        );

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo,pageSize,sort);

        Page<Review> reviewPage = reviewRepository.findByCourseId(course.getId(),pageable);

        Page<ReviewDto> reviewDtoPage = reviewPage.map(this::mapToDto);

        return new PageResponse<>(reviewDtoPage);
    }

    @Override
    public List<ReviewDto> getStudentReviews(Long studentId) {
        Student student = studentRepository.findById(studentId).orElseThrow(
                () -> new ResourceNotFoundException("Student", "id", studentId)
        );

        List<Review> reviews = reviewRepository.findByStudentId(student.getId());

        return reviews.stream().map(this::mapToDto).toList();
    }


    private ReviewDto mapToDto(Review review){
        ReviewDto reviewDto = modelMapper.map(review, ReviewDto.class);

        reviewDto.setStudentId(review.getStudent().getId());
        reviewDto.setStudentName(review.getStudent().getUsername());
        reviewDto.setCourseId(review.getCourse().getId());
        reviewDto.setCourseName(review.getCourse().getName());

        return reviewDto;
    }

}
