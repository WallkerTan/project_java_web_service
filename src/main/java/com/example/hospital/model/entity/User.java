package com.example.hospital.model.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.example.hospital.model.enums.UserRole;
import com.example.hospital.model.enums.UserStatus;
import com.example.hospital.utils.GlobalFuntion;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userName;
    private String email;
    private String password;
    @CreationTimestamp
    private LocalDateTime createAt;
    @UpdateTimestamp
    private LocalDateTime updateAt;



    private UserRole role = UserRole.PATIENT;
    private UserStatus status = UserStatus.ACTIVE;


    // 1 patient có thể có nhiều lịch hẹn
    @OneToMany(mappedBy = "patient")
    @JsonIgnore
    private List<Appoinment> patientAppoinment = new ArrayList<>();

    // 2 doctor có thể có nhiều lịch hẹn
    @OneToMany(mappedBy = "doctor")
    @JsonIgnore
    private List<Appoinment> doctorAppoinment = new ArrayList<>();

    //1 người dùng có thể có nhiều token hết hạn
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<TokenBlacklist> tokenBlacklists = new ArrayList<>();

    //1 người dùng có thể có nhiều rf token
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<RefreshToken> refreshTokens = new ArrayList<>();
}
