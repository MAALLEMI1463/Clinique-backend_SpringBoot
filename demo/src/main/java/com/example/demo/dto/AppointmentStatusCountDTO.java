package com.example.demo.dto;

/**
 * One slice of the appointments-by-status pie chart.
 * Returned as a list: [ {status:"PENDING", count:5}, ... ]
 */
public class AppointmentStatusCountDTO {

    private String status;
    private long count;

    public AppointmentStatusCountDTO() {}

    public AppointmentStatusCountDTO(String status, long count) {
        this.status = status;
        this.count  = count;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public long getCount() { return count; }
    public void setCount(long count) { this.count = count; }
}