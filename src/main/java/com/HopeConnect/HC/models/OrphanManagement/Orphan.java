package com.HopeConnect.HC.models.OrphanManagement;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Orphan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int age;
    private String educationStatus;
    private String healthCondition;

    @ManyToOne
    @JoinColumn(name = "orphanage_id")
    private Orphanage orphanage;

    // Getters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getEducationStatus() {
        return educationStatus;
    }

    public String getHealthCondition() {
        return healthCondition;
    }

    public Orphanage getOrphanage() {
        return orphanage;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setEducationStatus(String educationStatus) {
        this.educationStatus = educationStatus;
    }

    public void setHealthCondition(String healthCondition) {
        this.healthCondition = healthCondition;
    }

    public void setOrphanage(Orphanage orphanage) {
        this.orphanage = orphanage;
    }
}
