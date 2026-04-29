package com.pulsepriority.hospitalemergency.controller;

import com.pulsepriority.hospitalemergency.dto.PatientRequest;
import com.pulsepriority.hospitalemergency.model.Patient;
import com.pulsepriority.hospitalemergency.service.TriageService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public ResponseEntity<List<Patient>> queue(@RequestParam(required = false, defaultValue = "") String q) {
        return ResponseEntity.ok(triageService.currentQueue(q));
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
    public ResponseEntity<List<Patient>> history(@RequestParam(required = false, defaultValue = "") String q) {
        return ResponseEntity.ok(triageService.treatedHistory(q));
    }

    @GetMapping(value = "/history/export", produces = "text/csv")
    public ResponseEntity<String> exportHistoryCsv() {
        String header = "id,fullName,triageScore,symptoms,treated";
        String body = triageService.treatedHistory().stream()
                .map(p -> String.format("%d,%s,%d,%s,%s",
                        p.getId(),
                        sanitizeCsv(p.getFullName()),
                        p.getTriageScore(),
                        sanitizeCsv(p.getSymptoms()),
                        p.isTreated()))
                .collect(Collectors.joining("\n"));

        String csv = header + (body.isBlank() ? "" : "\n" + body);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("text/csv"))
                .header("Content-Disposition", "attachment; filename=treated-history.csv")
                .body(csv);
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> stats() {
        return ResponseEntity.ok(triageService.dashboardStats());
    }

    private String sanitizeCsv(String value) {
        if (value == null) {
            return "";
        }
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }
}
