package com.course.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.course.entity.Course;
import com.course.entity.Enrollment;
import com.course.entity.User;
import com.course.exception.ResourceNotFoundException;
import com.course.repository.CourseRepository;
import com.course.repository.EnrollmentRepository;
import com.course.repository.UserRepository;

@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public EnrollmentService(
            EnrollmentRepository enrollmentRepository,
            CourseRepository courseRepository,
            UserRepository userRepository) {

        this.enrollmentRepository = enrollmentRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    // ===============================
    // STUDENT → ENROLL IN COURSE
    // ===============================
    public Enrollment enrollStudent(String studentEmail, Long courseId) {

        User student = userRepository.findByEmail(studentEmail)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student not found")
                );

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Course not found")
                );

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        // ✅ enrolledDate is set automatically in constructor

        return enrollmentRepository.save(enrollment);
    }

    // ===============================
    // STUDENT → VIEW OWN ENROLLMENTS
    // ===============================
    public List<Enrollment> getEnrollmentsByStudent(String studentEmail) {

        User student = userRepository.findByEmail(studentEmail)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student not found")
                );

        return enrollmentRepository.findByStudentId(student.getId());
    }

    // ===============================
    // INSTRUCTOR → VIEW STUDENTS
    // ===============================
    public List<Enrollment> getStudentsByCourse(Long courseId, String instructorEmail) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Course not found")
                );

        if (!course.getInstructor().getEmail().equals(instructorEmail)) {
            throw new RuntimeException("Unauthorized access to this course");
        }

        return enrollmentRepository.findByCourseId(courseId);
    }
}
