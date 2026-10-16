package com.example.demo.repository;

import com.example.demo.model.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConsultationRepository extends JpaRepository<Consultation, Long> {

    boolean existsByAppointmentId(Long appointmentId);

    Optional<Consultation> findByAppointmentId(Long appointmentId);

    @Query("SELECT c FROM Consultation c " +
           "JOIN c.appointment a " +
           "JOIN a.patient p " +
           "WHERE LOWER(p.firstName) LIKE LOWER(CONCAT('%', :name, '%')) " +
           "   OR LOWER(p.lastName)  LIKE LOWER(CONCAT('%', :name, '%')) " +
           "ORDER BY c.createdAt DESC")
    List<Consultation> searchByPatientName(@Param("name") String name);

    @Query("SELECT c FROM Consultation c " +
           "JOIN c.appointment a " +
           "JOIN a.patient p " +
           "WHERE a.doctor.id = :doctorId " +
           "AND (LOWER(p.firstName) LIKE LOWER(CONCAT('%', :name, '%')) " +
           "  OR LOWER(p.lastName)  LIKE LOWER(CONCAT('%', :name, '%'))) " +
           "ORDER BY c.createdAt DESC")
    List<Consultation> searchByPatientNameAndDoctorId(
            @Param("name") String name,
            @Param("doctorId") Long doctorId);

    // ✅ NEW — Line chart: consultations per month for a given year
    // Returns Object[] { month(Integer), count(Long) }
    @Query("SELECT MONTH(c.createdAt), COUNT(c) " +
           "FROM Consultation c " +
           "WHERE YEAR(c.createdAt) = :year " +
           "GROUP BY MONTH(c.createdAt) " +
           "ORDER BY MONTH(c.createdAt) ASC")
    List<Object[]> countByMonthForYear(@Param("year") int year);

    // ✅ NEW — Bar chart: total consultations per doctor
    // Returns Object[] { doctorId(Long), firstName(String), lastName(String), count(Long) }
    @Query("SELECT a.doctor.id, a.doctor.firstName, a.doctor.lastName, COUNT(c) " +
           "FROM Consultation c " +
           "JOIN c.appointment a " +
           "GROUP BY a.doctor.id, a.doctor.firstName, a.doctor.lastName " +
           "ORDER BY COUNT(c) DESC")
    List<Object[]> countPerDoctor();

    // ✅ NEW — Multi-line chart: consultations per doctor per month
    // Returns Object[] { doctorId, firstName, lastName, month(Integer), count(Long) }
    @Query("SELECT a.doctor.id, a.doctor.firstName, a.doctor.lastName, " +
           "       MONTH(c.createdAt), COUNT(c) " +
           "FROM Consultation c " +
           "JOIN c.appointment a " +
           "WHERE YEAR(c.createdAt) = :year " +
           "GROUP BY a.doctor.id, a.doctor.firstName, a.doctor.lastName, MONTH(c.createdAt) " +
           "ORDER BY a.doctor.id ASC, MONTH(c.createdAt) ASC")
    List<Object[]> countPerDoctorPerMonth(@Param("year") int year);
}