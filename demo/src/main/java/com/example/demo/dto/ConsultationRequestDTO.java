package com.example.demo.dto;

/**
 * DTO received from the frontend when creating a consultation.
 *
 * NOT included (handled on the backend):
 *   - doctorId    → extracted from JWT token
 *   - createdAt   → set by @PrePersist
 *   - appointmentId → passed via URL path variable, not body
 */
public class ConsultationRequestDTO {

    private String note;
    private String symptoms;
    private String diagnosis;
    private String treatment;

    public ConsultationRequestDTO() {}

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public String getSymptoms() { return symptoms; }
    public void setSymptoms(String symptoms) { this.symptoms = symptoms; }

    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }

    public String getTreatment() { return treatment; }
    public void setTreatment(String treatment) { this.treatment = treatment; }
}