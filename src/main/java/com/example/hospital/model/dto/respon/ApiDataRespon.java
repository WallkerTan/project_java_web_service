package com.example.hospital.model.dto.respon;

import java.time.LocalDate;
import org.springframework.http.HttpStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ApiDataRespon<T> {
    private boolean status;
    private String message;
    private T data;
    private HttpStatus httpStatus;
}
