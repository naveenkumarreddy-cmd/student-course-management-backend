package com.course.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.course.entity.Course;
import com.course.entity.User;
import com.course.exception.ResourceNotFoundException;
import com.course.repository.CourseRepository;
import com.course.repository.UserRepository;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public CourseService(CourseRepository courseRepository,
                         UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    // =====================================
    // INSTRUCTOR → CREATE COURSE
    // =====================================
    public Course createCourseByEmail(String instructorEmail, Course course) {

        User instructor = userRepository.findByEmail(instructorEmail)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Instructor not found with email: " + instructorEmail
                        )
                );

        course.setInstructor(instructor);
        return courseRepository.save(course);
    }

    // =====================================
    // ADMIN / STUDENT → VIEW ALL COURSES
    // =====================================
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    // =====================================
    // INSTRUCTOR → VIEW OWN COURSES
    // =====================================
    public List<Course> getCoursesByInstructor(String instructorEmail) {

        User instructor = userRepository.findByEmail(instructorEmail)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Instructor not found with email: " + instructorEmail
                        )
                );

        return courseRepository.findByInstructorId(instructor.getId());
    }

    // =====================================
    // COURSE DETAILS
    // =====================================
    public Course getCourseById(Long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Course not found with id: " + courseId
                        )
                );
    }

    // =====================================
    // INSTRUCTOR → UPDATE COURSE
    // =====================================
    public Course updateCourseByInstructor(
            Long courseId,
            String instructorEmail,
            Course updatedCourse) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Course not found")
                );

        if (!course.getInstructor().getEmail().equals(instructorEmail)) {
            throw new RuntimeException("You are not allowed to update this course");
        }

        course.setTitle(updatedCourse.getTitle());
        course.setDescription(updatedCourse.getDescription());

        return courseRepository.save(course);
    }

    // =========================
    // ADMIN → DELETE COURSE
    // =========================
    public void deleteCourseById(Long id) {

        Course course = courseRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Course not found with id: " + id
                        )
                );

        courseRepository.delete(course);
    }
}
