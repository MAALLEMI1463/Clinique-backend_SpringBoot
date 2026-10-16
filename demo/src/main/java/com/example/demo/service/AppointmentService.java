package com.example.demo.service;

import com.example.demo.dto.AppointmentRequestDTO;
import com.example.demo.dto.AppointmentResponseDTO;
import com.example.demo.dto.DoctorResponseDTO;
import com.example.demo.dto.UpdateStatusDTO;
import com.example.demo.model.*;
import com.example.demo.repository.AppointmentRepository;
import com.example.demo.repository.PatientRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    // ── Clinic working hours ────────────────────────────────────────────────
    private static final LocalTime CLINIC_OPEN  = LocalTime.of(8, 0);   // 08:00
    private static final LocalTime CLINIC_CLOSE = LocalTime.of(16, 0);  // 16:00
    private static final int SLOT_DURATION_MINUTES = 20;

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository     patientRepository;
    private final UserRepository        userRepository;

    public AppointmentService(AppointmentRepository appointmentRepository,
                               PatientRepository patientRepository,
                               UserRepository userRepository) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository     = patientRepository;
        this.userRepository        = userRepository;
    }

    // ── CREATE appointment ──────────────────────────────────────────────────
    public AppointmentResponseDTO createAppointment(AppointmentRequestDTO request) {

        // 1) Extract STAFF from JWT (never from frontend)
        String username = getAuthenticatedUsername();
        User staff = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found: " + username));

        // 2) Load patient
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found: " + request.getPatientId()));

        // 3) Load doctor (must have DOCTOR role)
        User doctor = userRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found: " + request.getDoctorId()));

        if (doctor.getRole() != Role.DOCTOR) {
            throw new RuntimeException("Selected user is not a doctor.");
        }

        // 4) Date is always today
        LocalDate today = LocalDate.now();

        // 5) Auto-generate next available start time
        LocalTime startTime = findNextAvailableSlot(doctor.getId(), today);

        // 6) Build and save appointment
        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setStaff(staff);
        appointment.setDate(today);
        appointment.setStartTime(startTime);
        appointment.setStatus(AppointmentStatus.PENDING);

        Appointment saved = appointmentRepository.save(appointment);
        return toResponseDTO(saved);
    }

    // ── AUTO TIME GENERATION ────────────────────────────────────────────────
    /**
     * Finds the next available 20-minute slot for a doctor on a given date,
     * starting from the CURRENT TIME (not 08:00).
     *
     * Algorithm:
     *   1. Take LocalTime.now() and round it UP to the next 20-min boundary.
     *      e.g.  10:05 → 10:20
     *            10:20 → 10:20  (exact boundary is valid)
     *            10:21 → 10:40
     *   2. The search starts from max(CLINIC_OPEN, roundedUpNow).
     *      If it is before clinic opens (e.g. 07:30) we still start at 08:00.
     *   3. Walk forward in 20-min steps, skipping booked slots.
     *   4. If we reach CLINIC_CLOSE with no free slot, throw an exception.
     */
    private LocalTime findNextAvailableSlot(Long doctorId, LocalDate date) {
        List<Appointment> existing = appointmentRepository.findByDoctorIdAndDate(doctorId, date);

        // Collect all booked start times for O(1) lookup
        java.util.Set<LocalTime> bookedTimes = existing.stream()
                .map(Appointment::getStartTime)
                .collect(java.util.stream.Collectors.toSet());

        // ── Step 1: round current time UP to the next 20-min boundary ──────
        LocalTime now = LocalTime.now();
        LocalTime roundedNow = roundUpToNextSlot(now);

        // ── Step 2: search starts from max(CLINIC_OPEN, roundedNow) ─────────
        LocalTime slot = roundedNow.isBefore(CLINIC_OPEN) ? CLINIC_OPEN : roundedNow;

        // ── Step 3: find first free slot from that point forward ─────────────
        while (slot.isBefore(CLINIC_CLOSE)) {
            if (!bookedTimes.contains(slot)) {
                return slot; // ✅ First available slot at or after current time
            }
            slot = slot.plusMinutes(SLOT_DURATION_MINUTES);
        }

        throw new RuntimeException(
            "No available appointment slots for this doctor for the rest of today. " +
            "All remaining slots from " + roundedNow + " to " +
            CLINIC_CLOSE + " are fully booked."
        );
    }

    /**
     * Rounds a time UP to the nearest 20-minute slot boundary.
     *
     * Examples:
     *   10:00 → 10:00  (already on boundary)
     *   10:01 → 10:20
     *   10:19 → 10:20
     *   10:20 → 10:20  (already on boundary)
     *   10:21 → 10:40
     */
    private LocalTime roundUpToNextSlot(LocalTime time) {
        int totalMinutes = time.getHour() * 60 + time.getMinute();
        int remainder    = totalMinutes % SLOT_DURATION_MINUTES;

        if (remainder == 0) {
            // Already exactly on a slot boundary
            return LocalTime.of(time.getHour(), time.getMinute(), 0);
        }

        // Round up to next boundary
        int roundedMinutes = totalMinutes + (SLOT_DURATION_MINUTES - remainder);
        int hours   = roundedMinutes / 60;
        int minutes = roundedMinutes % 60;

        // Guard: if rounding pushes past midnight, cap at CLINIC_CLOSE
        if (hours >= 24) {
            return CLINIC_CLOSE;
        }

        return LocalTime.of(hours, minutes, 0);
    }

    // ── UPDATE STATUS ───────────────────────────────────────────────────────
    /**
     * Status transitions allowed:
     *   PENDING → CONFIRMED
     *   CONFIRMED → COMPLETED
     * Skipping is not allowed (e.g. PENDING → COMPLETED is rejected).
     */
    public AppointmentResponseDTO updateStatus(Long appointmentId, UpdateStatusDTO request) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found: " + appointmentId));

        AppointmentStatus currentStatus = appointment.getStatus();
        AppointmentStatus newStatus;

        try {
            newStatus = AppointmentStatus.valueOf(request.getStatus().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid status: " + request.getStatus() +
                    ". Allowed values: PENDING, CONFIRMED, COMPLETED");
        }

        // Validate transition
        if (currentStatus == AppointmentStatus.PENDING && newStatus == AppointmentStatus.CONFIRMED) {
            appointment.setStatus(AppointmentStatus.CONFIRMED);
        } else if (currentStatus == AppointmentStatus.CONFIRMED && newStatus == AppointmentStatus.COMPLETED) {
            appointment.setStatus(AppointmentStatus.COMPLETED);
        } else {
            throw new RuntimeException(
                "Invalid status transition: " + currentStatus + " → " + newStatus +
                ". Allowed: PENDING→CONFIRMED, CONFIRMED→COMPLETED"
            );
        }

        Appointment saved = appointmentRepository.save(appointment);
        return toResponseDTO(saved);
    }

    // ── GET DOCTOR'S APPOINTMENTS FOR TODAY ────────────────────────────────
    public List<AppointmentResponseDTO> getDoctorAppointmentsByDate(Long doctorId, LocalDate date) {
        return appointmentRepository.findByDoctorIdAndDate(doctorId, date)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    // ── GET ALL DOCTORS ─────────────────────────────────────────────────────
    public List<DoctorResponseDTO> getAllDoctors() {
        return userRepository.findByRole(Role.DOCTOR)
                .stream()
                .map(u -> new DoctorResponseDTO(u.getId(), u.getFirstName(), u.getLastName(), u.getUsername()))
                .collect(Collectors.toList());
    }

    // ── GET APPOINTMENT BY ID ───────────────────────────────────────────────
    public AppointmentResponseDTO getAppointmentById(Long id) {
        Appointment a = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found: " + id));
        return toResponseDTO(a);
    }

    // ── HELPER: extract username from Spring Security context ───────────────
    private String getAuthenticatedUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    // ── MAPPER: Appointment → AppointmentResponseDTO ────────────────────────
    private AppointmentResponseDTO toResponseDTO(Appointment a) {
        LocalTime endTime = a.getStartTime() != null
                ? a.getStartTime().plusMinutes(SLOT_DURATION_MINUTES)
                : null;

        return new AppointmentResponseDTO(
                a.getId(),
                a.getPatient()  != null ? a.getPatient().getId()        : null,
                a.getPatient()  != null ? a.getPatient().getFirstName()  : null,
                a.getPatient()  != null ? a.getPatient().getLastName()   : null,
                a.getDoctor()   != null ? a.getDoctor().getId()          : null,
                a.getDoctor()   != null ? a.getDoctor().getFirstName()   : null,
                a.getDoctor()   != null ? a.getDoctor().getLastName()    : null,
                a.getStaff()    != null ? a.getStaff().getId()           : null,
                a.getStaff()    != null ? a.getStaff().getUsername()     : null,
                a.getDate(),
                a.getStartTime(),
                endTime,
                a.getStatus()   != null ? a.getStatus().name()           : null,
                a.getCreatedAt()
        );
    }
}