package com.course.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.course.entity.Role;
import com.course.entity.User;
import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
	    

	
	 // login / find by email
    Optional<User> findByEmail(String email);

    // admin use: get all students or instructors
    List<User> findByRole(Role role);
    
    boolean existsByRole(Role role);
    
   



}
