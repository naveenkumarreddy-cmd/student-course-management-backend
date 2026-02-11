package com.course.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.course.entity.Role;
import com.course.entity.User;
import com.course.repository.UserRepository;

@Component
public class AdminSeeder {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${default.admin.name}")
    private String adminName;

    @Value("${default.admin.email}")
    private String adminEmail;

    @Value("${default.admin.password}")
    private String adminPassword;

    public AdminSeeder(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void createAdminIfNotExists() {

        boolean adminExists = userRepository.existsByRole(Role.ADMIN);

        if (!adminExists) {

            User admin = new User();
            admin.setName(adminName);
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setRole(Role.ADMIN);

            userRepository.save(admin);
        }
    }
}
