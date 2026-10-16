package com.example.demo.dto;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Lightweight DTO for displaying today's CONFIRMED appointments
 * on the Doctor Dashboard — only what the UI needs.
 */
public class AppointmentForDoctorDTO {

    private Long id;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String status;

    // Patient info
    private Long patientId;
    private String patientFirstName;
    private String patientLastName;

    // Staff who booked it
    private String staffUsername;

    // Whether a consultation already exists for this appointment
    private boolean consultationExists;

    public AppointmentForDoctorDTO() {}

    // ---- Getters & Setters ----
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }

    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }

    public String getPatientFirstName() { return patientFirstName; }
    public void setPatientFirstName(String patientFirstName) { this.patientFirstName = patientFirstName; }

    public String getPatientLastName() { return patientLastName; }
    public void setPatientLastName(String patientLastName) { this.patientLastName = patientLastName; }

    public String getStaffUsername() { return staffUsername; }
    public void setStaffUsername(String staffUsername) { this.staffUsername = staffUsername; }

    public boolean isConsultationExists() { return consultationExists; }
    public void setConsultationExists(boolean consultationExists) { this.consultationExists = consultationExists; }
}