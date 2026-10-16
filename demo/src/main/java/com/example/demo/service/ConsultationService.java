package com.example.demo.service;

import com.example.demo.dto.AppointmentForDoctorDTO;
import com.example.demo.dto.ConsultationRequestDTO;
import com.example.demo.dto.ConsultationResponseDTO;
import com.example.demo.model.*;
import com.example.demo.repository.AppointmentRepository;
import com.example.demo.repository.ConsultationRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConsultationService {

    private static final int SLOT_DURATION_MINUTES = 20;

    private final ConsultationRepository consultationRepository;
    private final AppointmentRepository  appointmentRepository;
    private final UserRepository         userRepository;

    public ConsultationService(ConsultationRepository consultationRepository,
                                AppointmentRepository appointmentRepository,
                                UserRepository userRepository) {
        this.consultationRepository = consultationRepository;
        this.appointmentRepository  = appointmentRepository;
        this.userRepository         = userRepository;
    }

    // ── GET today's CONFIRMED appointments for the authenticated doctor ─────
    public List<AppointmentForDoctorDTO> getTodayConfirmedAppointments() {
        User doctor = getAuthenticatedDoctor();

        List<Appointment> appointments = appointmentRepository
                .findByDoctorIdAndDateAndStatus(
                        doctor.getId(),
                        LocalDate.now(),
                        AppointmentStatus.CONFIRMED
                );

        return appointments.stream()
                .map(this::toAppointmentDTO)
                .collect(Collectors.toList());
    }

    // ── GET single appointment by ID (for doctor) ───────────────────────────
    public AppointmentForDoctorDTO getAppointmentById(Long appointmentId) {
        Appointment a = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException(
                        "Appointment not found: " + appointmentId));
        return toAppointmentDTO(a);
    }

    // ── CREATE consultation + auto-complete the appointment ─────────────────
    @Transactional
    public ConsultationResponseDTO createConsultation(Long appointmentId,
                                                       ConsultationRequestDTO request) {
        User doctor = getAuthenticatedDoctor();

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException(
                        "Appointment not found: " + appointmentId));

        if (!appointment.getDoctor().getId().equals(doctor.getId())) {
            throw new RuntimeException(
                    "This appointment does not belong to the authenticated doctor.");
        }

        if (appointment.getStatus() != AppointmentStatus.CONFIRMED) {
            throw new RuntimeException(
                    "Consultation can only be created for CONFIRMED appointments. " +
                    "Current status: " + appointment.getStatus());
        }

        if (consultationRepository.existsByAppointmentId(appointmentId)) {
            throw new RuntimeException(
                    "A consultation already exists for appointment #" + appointmentId);
        }

        Consultation consultation = new Consultation();
        consultation.setAppointment(appointment);
        consultation.setNote(request.getNote());
        consultation.setSymptoms(request.getSymptoms());
        consultation.setDiagnosis(request.getDiagnosis());
        consultation.setTreatment(request.getTreatment());

        Consultation saved = consultationRepository.save(consultation);

        // Auto-update appointment: CONFIRMED → COMPLETED
        appointment.setStatus(AppointmentStatus.COMPLETED);
        appointmentRepository.save(appointment);

        return toResponseDTO(saved);
    }

    // ── GET consultation by ID ───────────────────────────────────────────────
    public ConsultationResponseDTO getConsultationById(Long id) {
        Consultation c = consultationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Consultation not found: " + id));
        return toResponseDTO(c);
    }

    // ── SEARCH consultations by patient name — ALL doctors ──────────────────
    public List<ConsultationResponseDTO> searchByPatientName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new RuntimeException("Search term cannot be empty.");
        }
        return consultationRepository
                .searchByPatientName(name.trim())
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    // ── HELPER: get authenticated doctor ────────────────────────────────────
    private User getAuthenticatedDoctor() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException(
                        "Authenticated user not found: " + username));

        if (user.getRole() != Role.DOCTOR) {
            throw new RuntimeException(
                    "Access denied. Only DOCTOR role can manage consultations.");
        }

        return user;
    }

    // ── MAPPER: Appointment → AppointmentForDoctorDTO ───────────────────────
    private AppointmentForDoctorDTO toAppointmentDTO(Appointment a) {
        AppointmentForDoctorDTO dto = new AppointmentForDoctorDTO();
        dto.setId(a.getId());
        dto.setDate(a.getDate());
        dto.setStartTime(a.getStartTime());
        dto.setEndTime(a.getStartTime() != null
                ? a.getStartTime().plusMinutes(SLOT_DURATION_MINUTES) : null);
        dto.setStatus(a.getStatus() != null ? a.getStatus().name() : null);
        dto.setPatientId(a.getPatient().getId());
        dto.setPatientFirstName(a.getPatient().getFirstName());
        dto.setPatientLastName(a.getPatient().getLastName());
        dto.setStaffUsername(a.getStaff() != null ? a.getStaff().getUsername() : "—");
        dto.setConsultationExists(
                consultationRepository.existsByAppointmentId(a.getId()));
        return dto;
    }

    // ── MAPPER: Consultation → ConsultationResponseDTO ───────────────────────
    private ConsultationResponseDTO toResponseDTO(Consultation c) {
        ConsultationResponseDTO dto = new ConsultationResponseDTO();
        dto.setId(c.getId());
        dto.setCreatedAt(c.getCreatedAt());
        dto.setNote(c.getNote());
        dto.setSymptoms(c.getSymptoms());
        dto.setDiagnosis(c.getDiagnosis());
        dto.setTreatment(c.getTreatment());

        Appointment a = c.getAppointment();
        if (a != null) {
            dto.setAppointmentId(a.getId());
            dto.setAppointmentDate(a.getDate());
            dto.setAppointmentTime(a.getStartTime());
            dto.setAppointmentStatus(
                    a.getStatus() != null ? a.getStatus().name() : null);

            if (a.getPatient() != null) {
                dto.setPatientId(a.getPatient().getId());
                dto.setPatientFirstName(a.getPatient().getFirstName());
                dto.setPatientLastName(a.getPatient().getLastName());
            }

            if (a.getDoctor() != null) {
                dto.setDoctorId(a.getDoctor().getId());
                dto.setDoctorFirstName(a.getDoctor().getFirstName());
                dto.setDoctorLastName(a.getDoctor().getLastName());
            }
        }

        return dto;
    }
}
