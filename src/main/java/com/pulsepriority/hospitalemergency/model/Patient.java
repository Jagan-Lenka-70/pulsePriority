package com.pulsepriority.hospitalemergency.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;

    private int triageScore;

    private String symptoms;

    private boolean treated;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getTriageScore() {
        return triageScore;
    }

    public void setTriageScore(int triageScore) {
        this.triageScore = triageScore;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public boolean isTreated() {
        return treated;
    }

    public void setTreated(boolean treated) {
        this.treated = treated;
    }
}
