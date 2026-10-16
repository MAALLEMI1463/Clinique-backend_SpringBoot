package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:3000")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    /**
     * GET /api/admin/stats
     * Returns the 5 KPI counts for the statistics cards.
     * ROLE_ADMIN only.
     */
    @GetMapping("/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminStatsDTO> getStats() {
        return ResponseEntity.ok(adminService.getStats());
    }

    /**
     * GET /api/admin/charts/appointments-by-status
     * Pie chart data — PENDING / CONFIRMED / COMPLETED counts.
     */
    @GetMapping("/charts/appointments-by-status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AppointmentStatusCountDTO>> getAppointmentsByStatus() {
        return ResponseEntity.ok(adminService.getAppointmentsByStatus());
    }

    /**
     * GET /api/admin/charts/consultations-by-month
     * Line chart — consultations per month for the current year.
     */
    @GetMapping("/charts/consultations-by-month")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<MonthlyCountDTO>> getConsultationsByMonth() {
        return ResponseEntity.ok(adminService.getConsultationsByMonth());
    }

    /**
     * GET /api/admin/charts/consultations-per-doctor
     * Bar chart — total consultations for each doctor.
     */
    @GetMapping("/charts/consultations-per-doctor")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<DoctorConsultationCountDTO>> getConsultationsPerDoctor() {
        return ResponseEntity.ok(adminService.getConsultationsPerDoctor());
    }

    /**
     * GET /api/admin/charts/consultations-per-doctor-per-month
     * Multi-line chart — per-doctor monthly consultation counts.
     */
    @GetMapping("/charts/consultations-per-doctor-per-month")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<DoctorMonthlyCountDTO>> getConsultationsPerDoctorPerMonth() {
        return ResponseEntity.ok(adminService.getConsultationsPerDoctorPerMonth());
    }
}