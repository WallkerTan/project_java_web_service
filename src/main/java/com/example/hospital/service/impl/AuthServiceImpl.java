package com.example.hospital.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.hospital.mapper.UserMapper;
import com.example.hospital.model.dto.request.LoginRequest;
import com.example.hospital.model.dto.request.PasswordRequest;
import com.example.hospital.model.dto.request.UserRequest;
import com.example.hospital.model.dto.respon.UserRespon;
import com.example.hospital.model.entity.Appoinment;
import com.example.hospital.model.entity.RefreshToken;
import com.example.hospital.model.entity.TokenBlacklist;
import com.example.hospital.model.entity.User;
import com.example.hospital.model.enums.UserRole;
import com.example.hospital.model.enums.UserStatus;
import com.example.hospital.repository.AppoinmentRepository;
import com.example.hospital.repository.TokenBlacklistRepository;
import com.example.hospital.repository.UserRepository;
import com.example.hospital.security.JwtUtil;
import com.example.hospital.security.UserDetail;
import com.example.hospital.service.AuthService;
import com.example.hospital.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AppoinmentRepository appoinmentRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;
    private final TokenBlacklistRepository tokenBlacklistRepository;
    private final RefreshTokenService refreshTokenService;

    @Override
    public UserRespon login(LoginRequest request) {
        // xác thực username + password, sai thì tự ném 401
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUserName(), request.getPassword()));

        // lấy user từ DB
        User user = userRepository.findByUserName(request.getUserName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // wrap user vào UserDetail để JwtUtil dùng
        UserDetail userDetail = new UserDetail(user);

        // tạo refresh token lưu vào DB
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        // trả về thông tin user + 2 token
        return UserRespon.builder().id(user.getId()).userName(user.getUserName())
                .email(user.getEmail()).role(user.getRole())
                .accessToken(jwtUtil.generateAccessToken(userDetail)).refreshToken(refreshToken)
                .build();
    }

    @Override
    public UserRespon registerPatient(UserRequest request) {
        // kiểm tra email trùng
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // tạo user mới từ request
        User user = userMapper.toEntity(request, null);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(UserRole.PATIENT);
        user.setStatus(UserStatus.ACTIVE);

        // lưu DB, trả về thông tin user (không có token)
        return userMapper.toRespon(userRepository.save(user));
    }

    @Override
    public UserRespon registerDoctor(UserRequest request) {
        // kiểm tra email trùng
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // tạo user mới từ request
        User user = userMapper.toEntity(request, null);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(UserRole.DOCTOR);
        user.setStatus(UserStatus.ACTIVE);

        // lưu DB, trả về thông tin user (không có token)
        return userMapper.toRespon(userRepository.save(user));
    }

    @Override
    public UserRespon refreshToken(String token) {
        // kiểm tra refresh token còn hạn và chưa bị revoke
        RefreshToken refreshToken = refreshTokenService.verifyRefreshToken(token);

        // lấy user từ refresh token entity
        User user = refreshToken.getUser();
        UserDetail userDetail = new UserDetail(user);

        // sinh access token mới, giữ nguyên refresh token cũ
        RefreshToken newRefreshToken = refreshTokenService.createRefreshToken2(user, token);
        return UserRespon.builder().accessToken(jwtUtil.generateAccessToken(userDetail))
                .refreshToken(newRefreshToken) // Lấy string token
                .build();
    }

    @Override
    public void logout(String token) {
        // đưa access token vào blacklist, hết hạn tự xóa
        TokenBlacklist blacklist =
                TokenBlacklist
                        .builder().token(token).expiredAt(jwtUtil.extractExpiration(token)
                                .toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                        .build();
        tokenBlacklistRepository.save(blacklist);
    }

    @Override
    public boolean isLoggedIn(Long userId) {
        // false = có token trong blacklist = đã logout
        return !tokenBlacklistRepository.existsByUserId(userId);
    }

    @Override
    public void revokeToken(Long userId) {
        // thu hồi toàn bộ refresh token của user
        refreshTokenService.revokeAllTokensByUser(userId);

        // khóa tài khoản
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setStatus(UserStatus.lOCKED);
        userRepository.save(user);
    }

    @Override
    public void initData() {
        // Chỉ chạy nếu DB chưa có data
        if (userRepository.count() > 0)
            return;

        String password = passwordEncoder.encode("Pass@1234");

        // Admin
        User admin =
                userRepository.save(User.builder().userName("admin").email("admin@hospital.com")
                        .password(password).role(UserRole.ADMIN).status(UserStatus.ACTIVE).build());

        // 5 Bác sĩ
        List<User> doctors = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            doctors.add(userRepository.save(User.builder().userName("doctor" + i)
                    .email("doctor" + i + "@hospital.com").password(password).role(UserRole.DOCTOR)
                    .status(UserStatus.ACTIVE).build()));
        }

        // 5 Bệnh nhân
        List<User> patients = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            patients.add(userRepository.save(User.builder().userName("patient" + i)
                    .email("patient" + i + "@gmail.com").password(password).role(UserRole.PATIENT)
                    .status(UserStatus.ACTIVE).build()));
        }

        // 5 Appointment
        for (int i = 0; i < 5; i++) {
            appoinmentRepository.save(Appoinment.builder().doctor(doctors.get(i))
                    .patient(patients.get(i)).startTime(LocalDateTime.now().plusDays(i + 1))
                    .symptom("Triệu chứng " + (i + 1)).build());
        }

        System.out.println(">>> Init data successfully");
    }

    @Override
    public Boolean TransPassword(PasswordRequest passwordRequest, Long id) {
        User u = userRepository.findById(id).orElse(null);
        if (u == null) {
            return false;
        }
        if (u.getPassword() != passwordRequest.getPassword()) {
            return false;
        }

        String password = passwordEncoder.encode(passwordRequest.getNewPassword());
        u.setPassword(password);
        return true;
    }
}
