package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

// ===========================
// Doctor Dashboard Controller
// ===========================
@RestController
@RequestMapping("/api/doctor")
@CrossOrigin(origins = "http://localhost:3000")
class DoctorController {

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<Map<String, String>> doctorDashboard(Authentication authentication) {
        return ResponseEntity.ok(Map.of(
                "message", "Welcome to Doctor Dashboard",
                "username", authentication.getName(),
                "role", "DOCTOR"
        ));
    }
}

// ===========================
// Staff Dashboard Controller
// ===========================
@RestController
@RequestMapping("/api/staff")
@CrossOrigin(origins = "http://localhost:3000")
class StaffController {

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Map<String, String>> staffDashboard(Authentication authentication) {
        return ResponseEntity.ok(Map.of(
                "message", "Welcome to Staff Dashboard",
                "username", authentication.getName(),
                "role", "STAFF"
        ));
    }
}
