package com.course.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.course.entity.Course;
import com.course.entity.Enrollment;
import com.course.service.CourseService;
import com.course.service.EnrollmentService;

@RestController
@RequestMapping("/api/instructor")
@CrossOrigin
@PreAuthorize("hasRole('INSTRUCTOR')")
public class InstructorController {

    private final CourseService courseService;
    private final EnrollmentService enrollmentService;

    public InstructorController(CourseService courseService,
                                EnrollmentService enrollmentService) {
        this.courseService = courseService;
        this.enrollmentService = enrollmentService;
    }

    // ===============================
    // INSTRUCTOR → CREATE COURSE
    // ===============================
    @PostMapping("/courses")
    public Course createCourse(@RequestBody Course course,
                               Principal principal) {

        // principal.getName() = instructor email (from JWT)
        return courseService.createCourseByEmail(
                principal.getName(),
                course
        );
    }

    // ===============================
    // INSTRUCTOR → VIEW ENROLLED STUDENTS
    // ===============================
    @GetMapping("/courses/{courseId}/students")
    public List<Enrollment> getEnrolledStudents(@PathVariable Long courseId,
                                                 Principal principal) {

        return enrollmentService.getStudentsByCourse(
                courseId,
                principal.getName()
        );
    }
    
    
}
