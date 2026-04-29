package com.pulsepriority.hospitalemergency.repository;

import com.pulsepriority.hospitalemergency.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    List<Patient> findByTreatedFalse();
    List<Patient> findByTreatedFalseAndFullNameContainingIgnoreCase(String fullName);
    List<Patient> findByTreatedTrueOrderByIdDesc();
    List<Patient> findByTreatedTrueAndFullNameContainingIgnoreCaseOrderByIdDesc(String fullName);
    long countByTreatedFalse();
    long countByTreatedTrue();
}
