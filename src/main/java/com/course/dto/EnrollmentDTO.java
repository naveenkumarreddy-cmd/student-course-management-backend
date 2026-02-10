package com.course.dto;

import java.time.LocalDate;

public class EnrollmentDTO {

    private Long courseId;
    private String courseTitle;
    private LocalDate enrolledDate;
    private String instructorName;


    public EnrollmentDTO() {}

    // getters & setters

    public Long getCourseId() {
        return courseId;
    }
 
    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }
 
    public String getCourseTitle() {
        return courseTitle;
    }
 
    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }
 
    public LocalDate getEnrolledDate() {
        return enrolledDate;
    }
 
    public void setEnrolledDate(LocalDate enrolledDate) {
        this.enrolledDate = enrolledDate;
    }

	

	public String getInstructorName() {
	    return instructorName;
	}

	public void setInstructorName(String instructorName) {
	    this.instructorName = instructorName;
	}

    
}
