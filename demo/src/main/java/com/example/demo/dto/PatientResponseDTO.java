package com.example.demo.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO sent to the frontend with patient data.
 * Includes staffId and staffUsername for display purposes.
 */
public class PatientResponseDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String gender;
    private String phone;
    private LocalDateTime createdAt;

    // Staff info (who registered this patient)
    private Long staffId;
    private String staffUsername;

    public PatientResponseDTO() {}

    public PatientResponseDTO(Long id, String firstName, String lastName,
                               LocalDate birthDate, String gender, String phone,
                               LocalDateTime createdAt, Long staffId, String staffUsername) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.phone = phone;
        this.createdAt = createdAt;
        this.staffId = staffId;
        this.staffUsername = staffUsername;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Long getStaffId() { return staffId; }
    public void setStaffId(Long staffId) { this.staffId = staffId; }

    public String getStaffUsername() { return staffUsername; }
    public void setStaffUsername(String staffUsername) { this.staffUsername = staffUsername; }
}