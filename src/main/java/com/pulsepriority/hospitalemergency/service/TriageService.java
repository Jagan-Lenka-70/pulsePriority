package com.pulsepriority.hospitalemergency.service;

import com.pulsepriority.hospitalemergency.dto.PatientRequest;
import com.pulsepriority.hospitalemergency.model.Patient;
import com.pulsepriority.hospitalemergency.repository.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

@Service
public class TriageService {

    private final PatientRepository patientRepository;

    public TriageService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Transactional
    public Patient registerPatient(PatientRequest request) {
        Patient patient = new Patient();
        patient.setFullName(request.getFullName());
        patient.setTriageScore(request.getTriageScore());
        patient.setSymptoms(request.getSymptoms());
        patient.setTreated(false);
        return patientRepository.save(patient);
    }

    @Transactional(readOnly = true)
    public List<Patient> currentQueue() {
        return maxHeapOrderedPatients();
    }

    @Transactional(readOnly = true)
    public List<Patient> currentQueue(String query) {
        if (query == null || query.isBlank()) {
            return currentQueue();
        }
        return maxHeapOrderedPatients(patientRepository.findByTreatedFalseAndFullNameContainingIgnoreCase(query.trim()));
    }

    @Transactional
    public Patient treatNextPatient() {
        List<Patient> waitingPatients = patientRepository.findByTreatedFalse();
        if (waitingPatients.isEmpty()) {
            return null;
        }

        PriorityQueue<Patient> maxHeap = new PriorityQueue<>(
                Comparator.comparingInt(Patient::getTriageScore).reversed()
                        .thenComparing(Patient::getId)
        );
        maxHeap.addAll(waitingPatients);

        Patient next = maxHeap.poll();
        if (next == null) {
            return null;
        }

        next.setTreated(true);
        return patientRepository.save(next);
    }

    @Transactional(readOnly = true)
    public List<Patient> treatedHistory() {
        return patientRepository.findByTreatedTrueOrderByIdDesc();
    }

    @Transactional(readOnly = true)
    public List<Patient> treatedHistory(String query) {
        if (query == null || query.isBlank()) {
            return treatedHistory();
        }
        return patientRepository.findByTreatedTrueAndFullNameContainingIgnoreCaseOrderByIdDesc(query.trim());
    }

    @Transactional(readOnly = true)
    public Map<String, Long> dashboardStats() {
        long waiting = patientRepository.countByTreatedFalse();
        long treated = patientRepository.countByTreatedTrue();

        Map<String, Long> stats = new LinkedHashMap<>();
        stats.put("waiting", waiting);
        stats.put("treated", treated);
        stats.put("total", waiting + treated);
        return stats;
    }

    private List<Patient> maxHeapOrderedPatients() {
        return maxHeapOrderedPatients(patientRepository.findByTreatedFalse());
    }

    private List<Patient> maxHeapOrderedPatients(List<Patient> patients) {
        return patients.stream()
                .sorted(Comparator.comparingInt(Patient::getTriageScore).reversed()
                        .thenComparing(Patient::getId))
                .toList();
    }
}
