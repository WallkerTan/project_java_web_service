package com.example.hospital.model.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "appoinments")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
// 1 người dùng có thể có nhiều appoinment
public class Appoinment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime startTime;
    private String endTime;

    private String symptom;
    private String diagnosis;

    @CreationTimestamp
    private String createAt;
    @UpdateTimestamp
    private String upDateAt;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private User doctor;


    @ManyToOne
    @JoinColumn(name = "patient_id")
    private User patient;
}
