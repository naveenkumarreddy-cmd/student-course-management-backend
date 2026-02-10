package com.course.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(
    name = "enrollments",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"student_id", "course_id"})
    }
)
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(name = "enrolled_date")
    private LocalDate enrolledDate;

    public Enrollment() {
        this.enrolledDate = LocalDate.now();
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }
 
    public void setId(Long id) {
        this.id = id;
    }
 
    public User getStudent() {
        return student;
    }
 
    public void setStudent(User student) {
        this.student = student;
    }
 
    public Course getCourse() {
        return course;
    }
 
    public void setCourse(Course course) {
        this.course = course;
    }
 
    public LocalDate getEnrolledDate() {
        return enrolledDate;
    }
 
    public void setEnrolledDate(LocalDate enrolledDate) {
        this.enrolledDate = enrolledDate;
    }
}
