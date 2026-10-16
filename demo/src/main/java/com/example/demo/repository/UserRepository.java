package com.example.demo.repository;

import com.example.demo.model.Role;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByRole(Role role);

    List<User> findByRole(Role role);

    // ✅ NEW — used by AdminService for "Total Doctors" and "Total Staff" cards
    long countByRole(Role role);
}