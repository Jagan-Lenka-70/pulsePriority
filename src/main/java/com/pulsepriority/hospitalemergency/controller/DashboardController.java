package com.pulsepriority.hospitalemergency.controller;

import com.pulsepriority.hospitalemergency.dto.PatientRequest;
import com.pulsepriority.hospitalemergency.model.Patient;
import com.pulsepriority.hospitalemergency.service.TriageService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DashboardController {

    private final TriageService triageService;

    public DashboardController(TriageService triageService) {
        this.triageService = triageService;
    }

    @GetMapping("/")
    public String dashboard(@RequestParam(required = false, defaultValue = "") String q, Model model) {
        model.addAttribute("patientRequest", new PatientRequest());
        model.addAttribute("queue", triageService.currentQueue(q));
        model.addAttribute("history", triageService.treatedHistory(q));
        model.addAttribute("stats", triageService.dashboardStats());
        model.addAttribute("q", q);
        return "index";
    }

    @PostMapping("/patients")
    public String addPatient(@Valid @ModelAttribute("patientRequest") PatientRequest patientRequest,
                             BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("queue", triageService.currentQueue());
            model.addAttribute("history", triageService.treatedHistory());
            model.addAttribute("stats", triageService.dashboardStats());
            model.addAttribute("q", "");
            return "index";
        }

        triageService.registerPatient(patientRequest);
        return "redirect:/";
    }

    @PostMapping("/next")
    public String treatNextPatient() {
        triageService.treatNextPatient();
        return "redirect:/";
    }

    @ModelAttribute("badgeClassResolver")
    public java.util.function.Function<Integer, String> badgeClassResolver() {
        return score -> {
            if (score >= 8) {
                return "danger";
            }
            if (score >= 4) {
                return "warning";
            }
            return "success";
        };
    }

    @ModelAttribute("severityTextResolver")
    public java.util.function.Function<Integer, String> severityTextResolver() {
        return score -> {
            if (score >= 8) {
                return "Critical";
            }
            if (score >= 4) {
                return "Urgent";
            }
            return "Stable";
        };
    }
}
