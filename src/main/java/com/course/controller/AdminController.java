package com.course.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.course.dto.CourseDTO;
import com.course.dto.UserDTO;
import com.course.entity.Course;
import com.course.entity.Role;
import com.course.entity.User;
import com.course.service.CourseService;
import com.course.service.UserService;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserService userService;
    private final CourseService courseService;

    public AdminController(UserService userService,
                           CourseService courseService) {
        this.userService = userService;
        this.courseService = courseService;
    }

    // ===============================
    // CREATE INSTRUCTOR
    // ===============================
    @PostMapping("/create-instructor")
    public UserDTO createInstructor(@RequestBody User user) {

        user.setRole(Role.INSTRUCTOR);
        User savedUser = userService.register(user);
        return mapUser(savedUser);
    }

    // ===============================
    // CREATE ADMIN
    // ===============================
    @PostMapping("/create-admin")
    public UserDTO createAdmin(@RequestBody User user) {

        user.setRole(Role.ADMIN);
        User savedUser = userService.register(user);
        return mapUser(savedUser);
    }

    // ===============================
    // DELETE STUDENT
    // ===============================
    @DeleteMapping("/students/{id}")
    public String deleteStudent(@PathVariable Long id) {
        userService.deleteUserById(id);
        return "Student deleted successfully";
    }

    // ===============================
    // DELETE INSTRUCTOR
    // ===============================
    @DeleteMapping("/instructors/{id}")
    public String deleteInstructor(@PathVariable Long id) {
        userService.deleteUserById(id);
        return "Instructor deleted successfully";
    }

    // ===============================
    // VIEW ALL STUDENTS
    // ===============================
    @GetMapping("/students")
    public List<UserDTO> getAllStudents() {
        return userService.getAllStudents()
                .stream()
                .map(this::mapUser)
                .collect(Collectors.toList());
    }

    // ===============================
    // VIEW ALL INSTRUCTORS
    // ===============================
    @GetMapping("/instructors")
    public List<UserDTO> getAllInstructors() {
        return userService.getAllInstructors()
                .stream()
                .map(this::mapUser)
                .collect(Collectors.toList());
    }

    // ===============================
    // VIEW ALL ADMINS (READ ONLY)
    // ===============================
    @GetMapping("/admins")
    public List<UserDTO> getAllAdmins() {
        return userService.getAllAdmins()
                .stream()
                .map(this::mapUser)
                .collect(Collectors.toList());
    }

    // ===============================
    // VIEW ALL COURSES
    // ===============================
    @GetMapping("/courses")
    public List<CourseDTO> getAllCourses() {
        return courseService.getAllCourses()
                .stream()
                .map(this::mapCourse)
                .collect(Collectors.toList());
    }

    // ===============================
    // DELETE COURSE
    // ===============================
    @DeleteMapping("/courses/{id}")
    public String deleteCourse(@PathVariable Long id) {
        courseService.deleteCourseById(id);
        return "Course deleted successfully";
    }

    // ===============================
    // DTO MAPPERS
    // ===============================
    private UserDTO mapUser(User user) {
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }

    private CourseDTO mapCourse(Course course) {

        CourseDTO dto = new CourseDTO();
        dto.setId(course.getId());
        dto.setTitle(course.getTitle());
        dto.setDescription(course.getDescription());

        // ✅ NULL SAFE (IMPORTANT)
        if (course.getInstructor() != null) {
            dto.setInstructorId(course.getInstructor().getId());
            dto.setInstructorName(course.getInstructor().getName());
        }

        return dto;
    }
}
