package com.example.demo.dto;

import java.time.LocalDate;

/**
 * DTO received from the frontend when creating a new patient.
 * staff_id is intentionally EXCLUDED — it is extracted from the JWT token on the backend.
 */
public class PatientRequestDTO {

    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String gender;
    private String phone;

    public PatientRequestDTO() {}

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
}