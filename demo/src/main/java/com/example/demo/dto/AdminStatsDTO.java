package com.example.demo.dto;

/**
 * Single DTO returned by GET /api/admin/stats
 * Contains all KPI counts needed for the statistics cards.
 */
public class AdminStatsDTO {

    private long totalPatients;
    private long totalAppointments;
    private long totalConsultations;
    private long totalDoctors;
    private long totalStaff;

    public AdminStatsDTO() {}

    public AdminStatsDTO(long totalPatients, long totalAppointments,
                          long totalConsultations, long totalDoctors, long totalStaff) {
        this.totalPatients      = totalPatients;
        this.totalAppointments  = totalAppointments;
        this.totalConsultations = totalConsultations;
        this.totalDoctors       = totalDoctors;
        this.totalStaff         = totalStaff;
    }

    public long getTotalPatients()      { return totalPatients; }
    public long getTotalAppointments()  { return totalAppointments; }
    public long getTotalConsultations() { return totalConsultations; }
    public long getTotalDoctors()       { return totalDoctors; }
    public long getTotalStaff()         { return totalStaff; }

    public void setTotalPatients(long v)      { this.totalPatients = v; }
    public void setTotalAppointments(long v)  { this.totalAppointments = v; }
    public void setTotalConsultations(long v) { this.totalConsultations = v; }
    public void setTotalDoctors(long v)       { this.totalDoctors = v; }
    public void setTotalStaff(long v)         { this.totalStaff = v; }
}