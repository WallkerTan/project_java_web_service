package com.example.hospital.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserRequest {
    @NotBlank(message = "Ten khong duoc bo trong")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$",
            message = "Username chi duoc chu  chu hoa chu thuong va dau_")
    private String userName;

    @NotBlank(message = "Email khong duoc bo trong")
    @Email(message = "Email khong hop le")
    private String email;

    @NotBlank(message = "Password khong duoc bo trong")
    @Size(min = 8, max = 20, message = "Password phai tu 8-20 ky tu")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[@#$%.]).+$",
            message = "Password phai co chu hoa, so va ky tu dac biet")
    private String password;
}
