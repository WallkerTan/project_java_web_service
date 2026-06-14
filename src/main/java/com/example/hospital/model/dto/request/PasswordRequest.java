package com.example.hospital.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PasswordRequest {

    @NotBlank(message = "Password khong duoc bo trong")
    private String password;

    @NotBlank(message = "Password khong duoc bo trong")
    private String newPassword;
}
