package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.model.AppointmentStatus;
import com.example.demo.model.Role;
import com.example.demo.repository.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AdminService {

    private final PatientRepository     patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final ConsultationRepository consultationRepository;
    private final UserRepository         userRepository;

    public AdminService(PatientRepository patientRepository,
                         AppointmentRepository appointmentRepository,
                         ConsultationRepository consultationRepository,
                         UserRepository userRepository) {
        this.patientRepository      = patientRepository;
        this.appointmentRepository  = appointmentRepository;
        this.consultationRepository = consultationRepository;
        this.userRepository         = userRepository;
    }

    // ── 1) STATISTICS CARDS ──────────────────────────────────────────────────
    public AdminStatsDTO getStats() {
        long patients      = patientRepository.count();
        long appointments  = appointmentRepository.count();
        long consultations = consultationRepository.count();
        long doctors       = userRepository.countByRole(Role.DOCTOR);
        long staff         = userRepository.countByRole(Role.STAFF);

        return new AdminStatsDTO(patients, appointments, consultations, doctors, staff);
    }

    // ── 2a) PIE CHART — appointments by status ───────────────────────────────
    public List<AppointmentStatusCountDTO> getAppointmentsByStatus() {
        List<AppointmentStatusCountDTO> result = new ArrayList<>();

        for (AppointmentStatus status : AppointmentStatus.values()) {
            long count = appointmentRepository.countByStatus(status);
            result.add(new AppointmentStatusCountDTO(status.name(), count));
        }

        return result;
    }

    // ── 2b) LINE CHART — consultations by month (current year) ──────────────
    public List<MonthlyCountDTO> getConsultationsByMonth() {
        int currentYear = java.time.LocalDate.now().getYear();

        // Raw query returns Object[] { month(int), count(long) }
        List<Object[]> rows = consultationRepository.countByMonthForYear(currentYear);

        // Build a full 12-month list (fill missing months with 0)
        Map<Integer, Long> monthMap = rows.stream()
                .collect(Collectors.toMap(
                        r -> ((Number) r[0]).intValue(),
                        r -> ((Number) r[1]).longValue()
                ));

        List<MonthlyCountDTO> result = new ArrayList<>();
        for (int m = 1; m <= 12; m++) {
            result.add(new MonthlyCountDTO(m, currentYear, monthMap.getOrDefault(m, 0L)));
        }

        return result;
    }

    // ── 2c) BAR CHART — consultations per doctor ─────────────────────────────
    public List<DoctorConsultationCountDTO> getConsultationsPerDoctor() {
        List<Object[]> rows = consultationRepository.countPerDoctor();

        return rows.stream()
                .map(r -> new DoctorConsultationCountDTO(
                        ((Number) r[0]).longValue(),              // doctorId
                        (String) r[1] + " " + (String) r[2],     // firstName + lastName
                        ((Number) r[3]).longValue()               // count
                ))
                .collect(Collectors.toList());
    }

    // ── 2d) MULTI-LINE CHART — consultations per doctor per month ────────────
    public List<DoctorMonthlyCountDTO> getConsultationsPerDoctorPerMonth() {
        int currentYear = java.time.LocalDate.now().getYear();
        List<Object[]> rows = consultationRepository.countPerDoctorPerMonth(currentYear);

        return rows.stream()
                .map(r -> new DoctorMonthlyCountDTO(
                        ((Number) r[0]).longValue(),              // doctorId
                        (String) r[1] + " " + (String) r[2],     // firstName + lastName
                        ((Number) r[3]).intValue(),               // month
                        currentYear,
                        ((Number) r[4]).longValue()               // count
                ))
                .collect(Collectors.toList());
    }
}