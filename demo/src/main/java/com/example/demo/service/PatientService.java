package com.example.demo.service;

import com.example.demo.dto.PatientPageResponseDTO;
import com.example.demo.dto.PatientRequestDTO;
import com.example.demo.dto.PatientResponseDTO;
import com.example.demo.model.Patient;
import com.example.demo.model.User;
import com.example.demo.repository.PatientRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    public PatientService(PatientRepository patientRepository, UserRepository userRepository) {
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
    }

    // -------------------------------------------------------
    // CREATE — extract staff from JWT, never from request body
    // -------------------------------------------------------
    public PatientResponseDTO registerPatient(PatientRequestDTO request) {
        // Extract authenticated username from Spring Security context (set by JWT filter)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Load the STAFF user from DB
        User staff = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found: " + username));

        // Build and save patient
        Patient patient = new Patient();
        patient.setFirstName(request.getFirstName());
        patient.setLastName(request.getLastName());
        patient.setBirthDate(request.getBirthDate());
        patient.setGender(request.getGender());
        patient.setPhone(request.getPhone());
        patient.setStaff(staff);  // ✅ staff_id set from JWT, NOT from frontend

        Patient saved = patientRepository.save(patient);
        return toResponseDTO(saved);
    }

    // -------------------------------------------------------
    // READ — paginated list of all patients
    // -------------------------------------------------------
    public PatientPageResponseDTO getAllPatients(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Patient> patientPage = patientRepository.findAllByOrderByCreatedAtDesc(pageable);

        List<PatientResponseDTO> dtos = patientPage.getContent()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());

        return new PatientPageResponseDTO(
                dtos,
                patientPage.getNumber(),
                patientPage.getTotalPages(),
                patientPage.getTotalElements(),
                patientPage.getSize(),
                patientPage.isFirst(),
                patientPage.isLast()
        );
    }

    // -------------------------------------------------------
    // SEARCH — by firstName or lastName (partial, case-insensitive)
    // -------------------------------------------------------
    public List<PatientResponseDTO> searchPatients(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new RuntimeException("Search term cannot be empty");
        }
        return patientRepository.searchByName(name.trim())
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    // -------------------------------------------------------
    // GET BY ID
    // -------------------------------------------------------
    public PatientResponseDTO getPatientById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + id));
        return toResponseDTO(patient);
    }

    // -------------------------------------------------------
    // Mapper: Patient entity → PatientResponseDTO
    // -------------------------------------------------------
    private PatientResponseDTO toResponseDTO(Patient patient) {
        return new PatientResponseDTO(
                patient.getId(),
                patient.getFirstName(),
                patient.getLastName(),
                patient.getBirthDate(),
                patient.getGender(),
                patient.getPhone(),
                patient.getCreatedAt(),
                patient.getStaff() != null ? patient.getStaff().getId() : null,
                patient.getStaff() != null ? patient.getStaff().getUsername() : null
        );
    }
}