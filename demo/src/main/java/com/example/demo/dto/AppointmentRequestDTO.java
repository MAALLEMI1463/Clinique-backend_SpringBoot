package com.example.demo.dto;

/**
 * DTO received from the frontend when creating a new appointment.
 *
 * NOT included (extracted on the backend):
 *   - staffId   → extracted from JWT token
 *   - date      → always today (LocalDate.now())
 *   - startTime → auto-generated (next available slot)
 *   - status    → always defaults to PENDING
 */
public class AppointmentRequestDTO {

    private Long patientId;
    private Long doctorId;

    public AppointmentRequestDTO() {}

    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }

    public Long getDoctorId() { return doctorId; }
    public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }
}