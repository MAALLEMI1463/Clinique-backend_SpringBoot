package com.example.demo.repository;

import com.example.demo.model.Appointment;
import com.example.demo.model.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    /**
     * EXISTING — Get all appointments for a specific doctor on a specific date.
     */
    @Query("SELECT a FROM Appointment a " +
           "WHERE a.doctor.id = :doctorId AND a.date = :date " +
           "ORDER BY a.startTime ASC")
    List<Appointment> findByDoctorIdAndDate(
            @Param("doctorId") Long doctorId,
            @Param("date") LocalDate date);

    /**
     * EXISTING — Check if a time slot is already taken.
     */
    @Query("SELECT COUNT(a) > 0 FROM Appointment a " +
           "WHERE a.doctor.id = :doctorId AND a.date = :date AND a.startTime = :startTime")
    boolean existsByDoctorIdAndDateAndStartTime(
            @Param("doctorId") Long doctorId,
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime);

    /**
     * EXISTING — Get all appointments for a patient.
     */
    @Query("SELECT a FROM Appointment a WHERE a.patient.id = :patientId ORDER BY a.date DESC, a.startTime DESC")
    List<Appointment> findByPatientId(@Param("patientId") Long patientId);

    /**
     * NEW — Get today's CONFIRMED appointments for a specific doctor.
     * Used by the Doctor Dashboard to show only actionable appointments.
     */
    @Query("SELECT a FROM Appointment a " +
           "WHERE a.doctor.id = :doctorId " +
           "AND a.date = :date " +
           "AND a.status = :status " +
           "ORDER BY a.startTime ASC")
    List<Appointment> findByDoctorIdAndDateAndStatus(
            @Param("doctorId") Long doctorId,
            @Param("date") LocalDate date,
            @Param("status") AppointmentStatus status);

    // ✅ NEW — used by AdminService for the pie chart
    long countByStatus(AppointmentStatus status);
}