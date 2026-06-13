package com.example.hospital.model.dto.request;

import java.time.LocalDate;
import java.time.LocalDateTime;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AppoinmentRequest {

    @Min(0)
    @NotNull(message = "khong duoc de trong")
    private Long doctor_id;

    @Min(0)
    @NotNull(message = "khong duoc de trong")
    private Long patient_id;

    @NotNull(message = "Thoi gian bat dau khong duoc bo trong")
    @Future(message = "Thoi gian bat dau phai o tuong lai")
    private LocalDateTime startTime;

    @NotBlank(message = "Thoi gian ket thuc khong duoc bo trong")
    private String endTime;

    @NotBlank(message = "Trieu chung khong duoc bo trong")
    @Size(min = 10, max = 500, message = "Trieu chung phai tu 10 den 500 ky tu")
    private String symptom;


}
