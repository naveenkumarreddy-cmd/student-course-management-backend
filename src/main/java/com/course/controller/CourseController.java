package com.course.controller;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.course.dto.CourseDTO;
import com.course.entity.Course;
import com.course.service.CourseService;

@RestController
@RequestMapping("/api/courses")
@CrossOrigin
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    // ===============================
    // INSTRUCTOR → CREATE COURSE
    // ===============================
    @PostMapping("/create")
    public CourseDTO createCourse(
            @RequestBody Course course,
            Principal principal) {

        // ✅ FIXED METHOD NAME
        Course savedCourse =
                courseService.createCourseByEmail(principal.getName(), course);

        return mapToDTO(savedCourse);
    }

    // ===============================
    // PUBLIC → VIEW ALL COURSES
    // ===============================
    @GetMapping
    public List<CourseDTO> getAllCourses() {
        return courseService.getAllCourses()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // ===============================
    // INSTRUCTOR → VIEW OWN COURSES
    // ===============================
    @GetMapping("/my")
    public List<CourseDTO> getMyCourses(Principal principal) {

        return courseService.getCoursesByInstructor(principal.getName())
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // ===============================
    // COURSE DETAILS
    // ===============================
    @GetMapping("/{courseId}")
    public CourseDTO getCourseById(@PathVariable Long courseId) {
        return mapToDTO(courseService.getCourseById(courseId));
    }

    // ===============================
    // DTO MAPPER
    // ===============================
    private CourseDTO mapToDTO(Course course) {

        CourseDTO dto = new CourseDTO();
        dto.setId(course.getId());
        dto.setTitle(course.getTitle());
        dto.setDescription(course.getDescription());
        dto.setInstructorId(course.getInstructor().getId());
        dto.setInstructorName(course.getInstructor().getName());

        return dto;
    }
    
    @PutMapping("/{courseId}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public CourseDTO updateCourse(
            @PathVariable Long courseId,
            @RequestBody Course course,
            Principal principal) {

        Course updatedCourse =
                courseService.updateCourseByInstructor(
                        courseId,
                        principal.getName(),
                        course
                );

        return mapToDTO(updatedCourse);
    }

}
