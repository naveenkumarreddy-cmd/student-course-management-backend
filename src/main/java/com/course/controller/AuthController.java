package com.course.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.*;

import com.course.dto.LoginRequest;
import com.course.dto.UserDTO;
import com.course.entity.Role;
import com.course.entity.User;
import com.course.security.JwtUtil;
import com.course.service.UserService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    // ===============================
    // REGISTER (PUBLIC → STUDENT ONLY)
    // ===============================
    @PostMapping("/register")
    public UserDTO register(@RequestBody User user) {

        // 🔐 FORCE ROLE TO STUDENT
        user.setRole(Role.STUDENT);

        User savedUser = userService.register(user);

        return new UserDTO(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getRole()
        );
    }

    // ===============================
    // LOGIN
    // ===============================
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody LoginRequest request) {

        User user = userService.login(
                request.getEmail(),
                request.getPassword()
        );

        String token = jwtUtil.generateToken(
                user.getEmail(),
                user.getRole().name()
        );

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("user", new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        ));

        return response;
    }
}
