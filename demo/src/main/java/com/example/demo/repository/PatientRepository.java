package com.example.demo.repository;

import com.example.demo.model.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    /**
     * Paginated list of all patients — for the patient list page.
     * Ordered by createdAt descending (newest first).
     */
    Page<Patient> findAllByOrderByCreatedAtDesc(Pageable pageable);

    /**
     * Search by firstName OR lastName (case-insensitive, partial match).
     * Used for the patient search workflow before booking an appointment.
     */
    @Query("SELECT p FROM Patient p WHERE " +
           "LOWER(p.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR " +
           "LOWER(p.lastName)  LIKE LOWER(CONCAT('%', :name, '%')) " +
           "ORDER BY p.lastName ASC, p.firstName ASC")
    List<Patient> searchByName(@Param("name") String name);

    /**
     * Search by both firstName AND lastName simultaneously (exact pair match).
     * Used to prevent duplicate registration.
     */
    @Query("SELECT p FROM Patient p WHERE " +
           "LOWER(p.firstName) = LOWER(:firstName) AND " +
           "LOWER(p.lastName)  = LOWER(:lastName)")
    List<Patient> findByFirstNameAndLastNameIgnoreCase(
            @Param("firstName") String firstName,
            @Param("lastName") String lastName);
}