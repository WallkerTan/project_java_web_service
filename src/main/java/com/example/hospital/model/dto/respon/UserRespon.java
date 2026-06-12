package com.example.hospital.model.dto.respon;

import java.time.LocalDateTime;
import com.example.hospital.model.entity.RefreshToken;
import com.example.hospital.model.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserRespon {
    private Long id;
    private String userName;
    private String email;
    private String password;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private UserRole role;
    private String accessToken;
    private RefreshToken refreshToken;
}
