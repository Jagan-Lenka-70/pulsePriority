package com.pulsepriority.hospitalemergency.config;

import com.pulsepriority.hospitalemergency.dto.PatientRequest;
import com.pulsepriority.hospitalemergency.repository.PatientRepository;
import com.pulsepriority.hospitalemergency.service.TriageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DemoDataInitializer implements CommandLineRunner {

    private final TriageService triageService;
    private final PatientRepository patientRepository;

    @Value("${app.demo-data.enabled:true}")
    private boolean demoDataEnabled;

    public DemoDataInitializer(TriageService triageService, PatientRepository patientRepository) {
        this.triageService = triageService;
        this.patientRepository = patientRepository;
    }

    @Override
    public void run(String... args) {
        if (!demoDataEnabled || patientRepository.count() > 0) {
            return;
        }

        List<PatientRequest> demoPatients = List.of(
                build("Riya Sharma", 9, "Severe chest pain and breathlessness"),
                build("Arjun Singh", 7, "High fever with dehydration"),
                build("Neha Verma", 4, "Deep cut with controlled bleeding"),
                build("Aman Khan", 10, "Road trauma with low consciousness"),
                build("Pooja Das", 2, "Mild sprain and swelling")
        );

        demoPatients.forEach(triageService::registerPatient);
    }

    private PatientRequest build(String fullName, int score, String symptoms) {
        PatientRequest request = new PatientRequest();
        request.setFullName(fullName);
        request.setTriageScore(score);
        request.setSymptoms(symptoms);
        return request;
    }
}
