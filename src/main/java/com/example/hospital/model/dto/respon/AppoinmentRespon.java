package com.example.hospital.model.dto.respon;

import java.time.LocalDate;
import java.time.LocalDateTime;
import com.example.hospital.model.enums.AppoinmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AppoinmentRespon {

    private LocalDateTime startTime;
    private String endTime;

    private String symptom;
    private String diagnosis;
    private AppoinmentStatus status;

    private String createAt;
    private String upDateAt;

    private Long doctor_id;
    private Long patient_id;
}
