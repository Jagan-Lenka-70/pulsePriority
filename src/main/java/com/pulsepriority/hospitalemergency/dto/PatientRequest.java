package com.pulsepriority.hospitalemergency.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PatientRequest {

    @NotBlank
    private String fullName;

    @NotNull
    @Min(1)
    @Max(10)
    private Integer triageScore;

    @NotBlank
    private String symptoms;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Integer getTriageScore() {
        return triageScore;
    }

    public void setTriageScore(Integer triageScore) {
        this.triageScore = triageScore;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }
}
