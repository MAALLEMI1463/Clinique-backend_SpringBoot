package com.example.demo.config;

import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Check if an ADMIN user already exists
        boolean adminExists = userRepository.existsByRole(Role.ADMIN);

        if (!adminExists) {
            // Create default ADMIN account
            User admin = new User();
            admin.setFirstName("Admin");
            admin.setLastName("System");
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123")); // BCrypt encrypted
            admin.setRole(Role.ADMIN);

            userRepository.save(admin);
            System.out.println("=======================================================");
            System.out.println("  Default ADMIN account created successfully!");
            System.out.println("  Username: admin");
            System.out.println("  Password: admin123");
            System.out.println("=======================================================");
        } else {
            System.out.println("=======================================================");
            System.out.println("  ADMIN account already exists. Skipping creation.");
            System.out.println("=======================================================");
        }
    }
}
