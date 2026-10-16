package com.example.demo.dto;

/**
 * One bar in the consultations-per-doctor bar chart.
 */
public class DoctorConsultationCountDTO {

    private Long   doctorId;
    private String doctorName;   // "firstName lastName"
    private long   count;

    public DoctorConsultationCountDTO() {}

    public DoctorConsultationCountDTO(Long doctorId, String doctorName, long count) {
        this.doctorId   = doctorId;
        this.doctorName = doctorName;
        this.count      = count;
    }

    public Long   getDoctorId()   { return doctorId; }
    public String getDoctorName() { return doctorName; }
    public long   getCount()      { return count; }

    public void setDoctorId(Long doctorId)      { this.doctorId = doctorId; }
    public void setDoctorName(String doctorName){ this.doctorName = doctorName; }
    public void setCount(long count)            { this.count = count; }
}