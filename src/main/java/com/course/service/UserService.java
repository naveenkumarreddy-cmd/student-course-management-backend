package com.course.service;

import java.util.Collection;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.course.dto.UserDTO;
import com.course.entity.Role;
import com.course.entity.User;
import com.course.exception.BadRequestException;
import com.course.exception.ResourceNotFoundException;
import com.course.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // ✅ PasswordEncoder injected
    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // =========================
    // REGISTER
    // =========================
    public User register(User user) {

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new BadRequestException("Email already registered");
        }

        // 🔐 HASH PASSWORD BEFORE SAVING
        user.setPassword(
                passwordEncoder.encode(user.getPassword())
        );

        return userRepository.save(user);
    }

    // =========================
    // LOGIN
    // =========================
    public User login(String email, String rawPassword) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Invalid credentials")
                );

        // 🔐 BCrypt comparison (NOT string equals)
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new BadRequestException("Invalid credentials");
        }

        return user;
    }

    // =========================
    // ADMIN METHODS
    // =========================
    public List<User> getAllStudents() {
        return userRepository.findByRole(Role.STUDENT);
    }

    public List<User> getAllInstructors() {
        return userRepository.findByRole(Role.INSTRUCTOR);
    }
    
    public List<User> getAllAdmins() {
        return userRepository.findByRole(Role.ADMIN);
    }


    // =========================
    // COMMON
    // =========================
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with email: " + email)
                );
    }
    
 // =========================
 // DELETE USER (ADMIN ONLY)
 // =========================
 public void deleteUserById(Long id) {

     User user = userRepository.findById(id)
             .orElseThrow(() ->
                     new ResourceNotFoundException("User not found with id: " + id)
             );

     // Optional safety: prevent deleting ADMIN
     if (user.getRole() == Role.ADMIN) {
         throw new BadRequestException("Admin users cannot be deleted");
     }

     userRepository.delete(user);
 }


  
}
