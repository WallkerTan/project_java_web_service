

package com.example.hospital.controller.publics;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.hospital.model.dto.request.LoginRequest;
import com.example.hospital.model.dto.request.UserRequest;
import com.example.hospital.model.dto.respon.ApiDataRespon;
import com.example.hospital.model.dto.respon.UserRespon;
import com.example.hospital.service.impl.AppoinmentServiceImpl;
import com.example.hospital.service.impl.AuthServiceImpl;
import com.example.hospital.service.impl.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequiredArgsConstructor
@RequestMapping("hospital/auth")
public class AuthController {
        private final UserServiceImpl uImpl;
        private final AppoinmentServiceImpl aImpl;
        private final AuthServiceImpl authServiceImpl;

        // đăng kí
        // Them
        @PostMapping("/create/patient")
        public ResponseEntity<ApiDataRespon<Boolean>> createPatient(
                        @Valid @RequestBody UserRequest request) {
                Boolean isCreated = uImpl.create(request);
                ApiDataRespon<Boolean> response = ApiDataRespon.<Boolean>builder().status(true)
                                .message("Tao thanh cong").data(isCreated)
                                .httpStatus(HttpStatus.CREATED).build();
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }

        @GetMapping("/gennerate")
        public ResponseEntity<ApiDataRespon<Void>> gennerate() {
                authServiceImpl.initData();
                return new ResponseEntity<>(
                                new ApiDataRespon<>(true, "gen thanh cong", null, HttpStatus.OK),
                                HttpStatus.OK);
        }


        // Them bac si
        @PostMapping("/create/doctor")
        public ResponseEntity<ApiDataRespon<Boolean>> createDoctor(
                        @Valid @RequestBody UserRequest request) {
                Boolean isCreated = uImpl.createDoctor(request);
                ApiDataRespon<Boolean> response = ApiDataRespon.<Boolean>builder().status(true)
                                .message("Tao bac si thanh cong").data(isCreated)
                                .httpStatus(HttpStatus.CREATED).build();
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }


        // Them admin
        @PostMapping
        public ResponseEntity<ApiDataRespon<Boolean>> createAdmin(
                        @Valid @RequestBody UserRequest request) {
                Boolean isCreated = uImpl.createAdmin(request);
                ApiDataRespon<Boolean> response = ApiDataRespon.<Boolean>builder().status(true)
                                .message("Tao admin thanh cong").data(isCreated)
                                .httpStatus(HttpStatus.CREATED).build();
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }

        // login
        @PostMapping("/login")
        public ResponseEntity<ApiDataRespon<UserRespon>> login(
                        @Valid @RequestBody LoginRequest request) {
                return ResponseEntity.ok(new ApiDataRespon<>(true, "Đăng nhập thành công",
                                authServiceImpl.login(request), HttpStatus.OK));
        }


        // Logout
        @PostMapping("/logout")
        public ResponseEntity<ApiDataRespon<Void>> logout(
                        @RequestHeader("Authorization") String authHeader) {
                String token = authHeader.substring(7); // bỏ "Bearer "
                authServiceImpl.logout(token);
                return ResponseEntity.ok(new ApiDataRespon<>(true, "Đăng xuất thành công", null,
                                HttpStatus.OK));
        }

        // refest@PostMapping("/refresh")
        public ResponseEntity<ApiDataRespon<UserRespon>> refreshToken(
                        @RequestBody String refreshToken) {
                return new ResponseEntity<>(new ApiDataRespon<>(true, "tạo rf token thanh cong",
                                authServiceImpl.refreshToken(refreshToken), HttpStatus.OK),
                                HttpStatus.OK);
        }

}
