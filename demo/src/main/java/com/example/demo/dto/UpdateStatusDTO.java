package com.example.demo.dto;

public class UpdateStatusDTO {

    private String status; // "CONFIRMED" or "COMPLETED"

    public UpdateStatusDTO() {}

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}