package com.example.demo.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

public class AppointmentResponseDTO {

    private Long id;

    // Patient
    private Long patientId;
    private String patientFirstName;
    private String patientLastName;

    // Doctor
    private Long doctorId;
    private String doctorFirstName;
    private String doctorLastName;

    // Staff
    private Long staffId;
    private String staffUsername;

    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;   // startTime + 20 minutes, computed
    private String status;
    private LocalDateTime createdAt;

    public AppointmentResponseDTO() {}

    public AppointmentResponseDTO(Long id,
                                   Long patientId, String patientFirstName, String patientLastName,
                                   Long doctorId, String doctorFirstName, String doctorLastName,
                                   Long staffId, String staffUsername,
                                   LocalDate date, LocalTime startTime, LocalTime endTime,
                                   String status, LocalDateTime createdAt) {
        this.id = id;
        this.patientId = patientId;
        this.patientFirstName = patientFirstName;
        this.patientLastName = patientLastName;
        this.doctorId = doctorId;
        this.doctorFirstName = doctorFirstName;
        this.doctorLastName = doctorLastName;
        this.staffId = staffId;
        this.staffUsername = staffUsername;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.createdAt = createdAt;
    }

    // ---- Getters & Setters ----
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }

    public String getPatientFirstName() { return patientFirstName; }
    public void setPatientFirstName(String patientFirstName) { this.patientFirstName = patientFirstName; }

    public String getPatientLastName() { return patientLastName; }
    public void setPatientLastName(String patientLastName) { this.patientLastName = patientLastName; }

    public Long getDoctorId() { return doctorId; }
    public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }

    public String getDoctorFirstName() { return doctorFirstName; }
    public void setDoctorFirstName(String doctorFirstName) { this.doctorFirstName = doctorFirstName; }

    public String getDoctorLastName() { return doctorLastName; }
    public void setDoctorLastName(String doctorLastName) { this.doctorLastName = doctorLastName; }

    public Long getStaffId() { return staffId; }
    public void setStaffId(Long staffId) { this.staffId = staffId; }

    public String getStaffUsername() { return staffUsername; }
    public void setStaffUsername(String staffUsername) { this.staffUsername = staffUsername; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }

    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}