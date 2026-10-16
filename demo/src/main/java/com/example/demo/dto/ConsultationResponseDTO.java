package com.example.demo.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ConsultationResponseDTO {

    private Long id;
    private LocalDateTime createdAt;
    private String note;
    private String symptoms;
    private String diagnosis;
    private String treatment;

    // Appointment info
    private Long appointmentId;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private String appointmentStatus;

    // Patient info (via Appointment → Patient)
    private Long patientId;
    private String patientFirstName;
    private String patientLastName;

    // Doctor info (via Appointment → Doctor)
    private Long doctorId;
    private String doctorFirstName;
    private String doctorLastName;

    public ConsultationResponseDTO() {}

    // ---- Getters & Setters ----
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public String getSymptoms() { return symptoms; }
    public void setSymptoms(String symptoms) { this.symptoms = symptoms; }

    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }

    public String getTreatment() { return treatment; }
    public void setTreatment(String treatment) { this.treatment = treatment; }

    public Long getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Long appointmentId) { this.appointmentId = appointmentId; }

    public LocalDate getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(LocalDate appointmentDate) { this.appointmentDate = appointmentDate; }

    public LocalTime getAppointmentTime() { return appointmentTime; }
    public void setAppointmentTime(LocalTime appointmentTime) { this.appointmentTime = appointmentTime; }

    public String getAppointmentStatus() { return appointmentStatus; }
    public void setAppointmentStatus(String appointmentStatus) { this.appointmentStatus = appointmentStatus; }

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
}