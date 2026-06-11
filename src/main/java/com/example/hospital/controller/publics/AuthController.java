

package com.example.hospital.controller.publics;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.hospital.model.dto.request.UserRequest;
import com.example.hospital.model.dto.respon.ApiDataRespon;
import com.example.hospital.service.impl.AppoinmentServiceImpl;
import com.example.hospital.service.impl.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping("hospital/auth")
public class AuthController {
    private final UserServiceImpl uImpl;
    private final AppoinmentServiceImpl aImpl;

    // đăng kí

    // Them
    @PostMapping("/create/patient")
    public ResponseEntity<ApiDataRespon<Boolean>> createPatient(
            @Valid @RequestBody UserRequest request) {
        Boolean isCreated = uImpl.create(request);
        ApiDataRespon<Boolean> response = ApiDataRespon.<Boolean>builder().status(true)
                .message("Tao thanh cong").data(isCreated).httpStatus(HttpStatus.CREATED).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    // Them bac si
    @PostMapping("/create/doctor")
    public ResponseEntity<ApiDataRespon<Boolean>> createDoctor(
            @Valid @RequestBody UserRequest request) {
        Boolean isCreated = uImpl.createDoctor(request);
        ApiDataRespon<Boolean> response =
                ApiDataRespon.<Boolean>builder().status(true).message("Tao bac si thanh cong")
                        .data(isCreated).httpStatus(HttpStatus.CREATED).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    // Them admin
    @PostMapping
    public ResponseEntity<ApiDataRespon<Boolean>> createAdmin(
            @Valid @RequestBody UserRequest request) {
        Boolean isCreated = uImpl.createAdmin(request);
        ApiDataRespon<Boolean> response =
                ApiDataRespon.<Boolean>builder().status(true).message("Tao admin thanh cong")
                        .data(isCreated).httpStatus(HttpStatus.CREATED).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    // đăng nhập
    // login
    // refest
    // đổi mk
}
