package com.pulsepriority.hospitalemergency.service;

import com.pulsepriority.hospitalemergency.dto.PatientRequest;
import com.pulsepriority.hospitalemergency.model.Patient;
import com.pulsepriority.hospitalemergency.repository.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
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

    private List<Patient> maxHeapOrderedPatients() {
        return patientRepository.findByTreatedFalse().stream()
                .sorted(Comparator.comparingInt(Patient::getTriageScore).reversed()
                        .thenComparing(Patient::getId))
                .toList();
    }
}
