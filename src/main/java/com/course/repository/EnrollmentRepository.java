package com.course.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.course.entity.Enrollment;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    List<Enrollment> findByStudentId(Long studentId);

    @Query("""
        SELECT e FROM Enrollment e
        WHERE e.course.instructor.id = :instructorId
    """)
    List<Enrollment> findEnrollmentsByInstructor(Long instructorId);
    
    List<Enrollment> findByCourseId(Long courseId);
}
