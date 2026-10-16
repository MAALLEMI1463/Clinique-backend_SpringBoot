package com.example.demo.controller;

import com.example.demo.dto.AppointmentRequestDTO;
import com.example.demo.dto.AppointmentResponseDTO;
import com.example.demo.dto.DoctorResponseDTO;
import com.example.demo.dto.UpdateStatusDTO;
import com.example.demo.service.AppointmentService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/staff/appointments")
@CrossOrigin(origins = "http://localhost:3000")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    /**
     * GET /api/staff/appointments/doctors
     * Returns all users with role=DOCTOR.
     * Used to populate the doctor selection cards.
     */
    @GetMapping("/doctors")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<List<DoctorResponseDTO>> getAllDoctors() {
        return ResponseEntity.ok(appointmentService.getAllDoctors());
    }

    /**
     * POST /api/staff/appointments
     * Create a new appointment.
     * - staffId extracted from JWT (not sent by frontend)
     * - date = today (auto)
     * - startTime = next available slot (auto)
     * - status = PENDING (auto)
     */
    @PostMapping
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<?> createAppointment(@RequestBody AppointmentRequestDTO request) {
        try {
            AppointmentResponseDTO response = appointmentService.createAppointment(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * GET /api/staff/appointments/{id}
     * Get a single appointment by ID.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<?> getAppointmentById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(appointmentService.getAppointmentById(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * GET /api/staff/appointments/doctor/{doctorId}?date=today
     * Get all appointments for a doctor on a specific date.
     * date param defaults to today if not provided.
     * Example: /api/staff/appointments/doctor/3?date=today
     */
    @GetMapping("/doctor/{doctorId}")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<List<AppointmentResponseDTO>> getDoctorAppointments(
            @PathVariable Long doctorId,
            @RequestParam(required = false) String date) {

        LocalDate targetDate;
        if (date == null || date.equalsIgnoreCase("today")) {
            targetDate = LocalDate.now();
        } else {
            targetDate = LocalDate.parse(date);
        }

        return ResponseEntity.ok(appointmentService.getDoctorAppointmentsByDate(doctorId, targetDate));
    }

    /**
     * PATCH /api/staff/appointments/{id}/status
     * Update appointment status.
     * Allowed transitions: PENDING→CONFIRMED, CONFIRMED→COMPLETED
     * Body: { "status": "CONFIRMED" }
     */
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long id,
            @RequestBody UpdateStatusDTO request) {
        try {
            AppointmentResponseDTO response = appointmentService.updateStatus(id, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}