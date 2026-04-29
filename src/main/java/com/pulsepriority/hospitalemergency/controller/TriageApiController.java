package com.pulsepriority.hospitalemergency.controller;

import com.pulsepriority.hospitalemergency.dto.PatientRequest;
import com.pulsepriority.hospitalemergency.model.Patient;
import com.pulsepriority.hospitalemergency.service.TriageService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/triage")
public class TriageApiController {

    private final TriageService triageService;

    public TriageApiController(TriageService triageService) {
        this.triageService = triageService;
    }

    @PostMapping("/patients")
    public ResponseEntity<Patient> addPatient(@Valid @RequestBody PatientRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(triageService.registerPatient(request));
    }

    @GetMapping("/queue")
    public ResponseEntity<List<Patient>> queue() {
        return ResponseEntity.ok(triageService.currentQueue());
    }

    @PostMapping("/next")
    public ResponseEntity<?> nextPatient() {
        Patient next = triageService.treatNextPatient();
        if (next == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "No patients waiting"));
        }
        return ResponseEntity.ok(next);
    }

    @GetMapping("/history")
    public ResponseEntity<List<Patient>> history() {
        return ResponseEntity.ok(triageService.treatedHistory());
    }
}
