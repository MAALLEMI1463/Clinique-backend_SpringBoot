package com.example.demo.controller;

import com.example.demo.dto.AppointmentForDoctorDTO;
import com.example.demo.dto.ConsultationRequestDTO;
import com.example.demo.dto.ConsultationResponseDTO;
import com.example.demo.service.ConsultationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctor")
@CrossOrigin(origins = "http://localhost:3000")
public class ConsultationController {

    private final ConsultationService consultationService;

    public ConsultationController(ConsultationService consultationService) {
        this.consultationService = consultationService;
    }

    /**
     * GET /api/doctor/appointments/today
     * Returns today's CONFIRMED appointments for the authenticated doctor.
     */
    @GetMapping("/appointments/today")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<List<AppointmentForDoctorDTO>> getTodayAppointments() {
        return ResponseEntity.ok(consultationService.getTodayConfirmedAppointments());
    }

    /**
     * GET /api/doctor/appointments/{id}
     * ✅ FIX 1: Fetch a single appointment by ID, accessible by DOCTOR role.
     * Previously the frontend was calling /api/staff/appointments/{id} which
     * returns 403 for DOCTOR users. This endpoint fixes that.
     */
    @GetMapping("/appointments/{id}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<?> getAppointmentById(@PathVariable Long id) {
        try {
            AppointmentForDoctorDTO dto = consultationService.getAppointmentById(id);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * POST /api/doctor/consultations/appointment/{appointmentId}
     * Create a new consultation. doctorId extracted from JWT — never sent by frontend.
     * Automatically sets appointment status to COMPLETED after saving.
     */
    @PostMapping("/consultations/appointment/{appointmentId}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<?> createConsultation(
            @PathVariable Long appointmentId,
            @RequestBody ConsultationRequestDTO request) {
        try {
            ConsultationResponseDTO response =
                    consultationService.createConsultation(appointmentId, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * GET /api/doctor/consultations/{id}
     * Get a single consultation by ID (read-only details view).
     */
    @GetMapping("/consultations/{id}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<?> getConsultationById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(consultationService.getConsultationById(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * GET /api/doctor/consultations/search?name=john
     * ✅ FIX 2: Search consultations by patient name across ALL doctors.
     * Previously restricted to current doctor's consultations only.
     */
    @GetMapping("/consultations/search")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<?> searchConsultations(@RequestParam String name) {
        try {
            List<ConsultationResponseDTO> results =
                    consultationService.searchByPatientName(name);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
