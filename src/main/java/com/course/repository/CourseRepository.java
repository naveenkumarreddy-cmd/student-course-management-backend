package com.course.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.course.entity.Course;

public interface CourseRepository extends JpaRepository<Course,Long > {
	
	 // instructor: see their own courses
    List<Course> findByInstructorId(Long instructorId);

}
