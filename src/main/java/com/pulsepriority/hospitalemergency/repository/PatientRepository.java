package com.pulsepriority.hospitalemergency.repository;

import com.pulsepriority.hospitalemergency.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    List<Patient> findByTreatedFalse();
    List<Patient> findByTreatedTrueOrderByIdDesc();
}
