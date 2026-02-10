package com.course.controller;
import java.util.stream.Collectors;


import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.*;

import com.course.dto.EnrollmentDTO;
import com.course.entity.Enrollment;
import com.course.service.EnrollmentService;

@RestController
@RequestMapping("/api/enrollments")
@CrossOrigin
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    // ==================================
    // STUDENT → ENROLL IN COURSE
    // ==================================
    @PostMapping("/course/{courseId}")
    public EnrollmentDTO enrollStudent(
            @PathVariable Long courseId,
            Principal principal) {

        // principal.getName() = student email from JWT
        Enrollment enrollment =
                enrollmentService.enrollStudent(principal.getName(), courseId);

        return mapToDTO(enrollment);
    }

    // ==================================
    // STUDENT → VIEW ENROLLED COURSES
    // ==================================
    @GetMapping("/my")
    public List<EnrollmentDTO> getMyEnrollments(Principal principal) {

        return enrollmentService.getEnrollmentsByStudent(principal.getName())
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // ==================================
    // DTO MAPPER
    // ==================================
    private EnrollmentDTO mapToDTO(Enrollment enrollment) {

        EnrollmentDTO dto = new EnrollmentDTO();
        dto.setCourseId(enrollment.getCourse().getId());
        dto.setCourseTitle(enrollment.getCourse().getTitle());
        dto.setEnrolledDate(enrollment.getEnrolledDate());
        dto.setInstructorName(enrollment.getCourse().getInstructor().getName());
                

        return dto;
    }
}
