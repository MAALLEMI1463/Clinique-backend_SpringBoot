package com.example.demo.controller;

import com.example.demo.dto.PatientPageResponseDTO;
import com.example.demo.dto.PatientRequestDTO;
import com.example.demo.dto.PatientResponseDTO;
import com.example.demo.service.PatientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/staff/patients")
@CrossOrigin(origins = "http://localhost:3000")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    /**
     * POST /api/staff/patients
     * Register a new patient.
     * staff_id is extracted from JWT — frontend does NOT send it.
     */
    @PostMapping
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<?> registerPatient(@RequestBody PatientRequestDTO request) {
        try {
            PatientResponseDTO response = patientService.registerPatient(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * GET /api/staff/patients?page=0&size=10
     * Get all patients with pagination.
     */
    @GetMapping
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<PatientPageResponseDTO> getAllPatients(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(patientService.getAllPatients(page, size));
    }

    /**
     * GET /api/staff/patients/search?name=john
     * Search patients by firstName or lastName (partial, case-insensitive).
     * Used for the search-before-appointment workflow.
     */
    @GetMapping("/search")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<?> searchPatients(@RequestParam String name) {
        try {
            List<PatientResponseDTO> results = patientService.searchPatients(name);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * GET /api/staff/patients/{id}
     * Get a single patient by ID.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<?> getPatientById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(patientService.getPatientById(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}