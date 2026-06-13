package com.example.hospital.controller.auth;

import com.example.hospital.model.dto.request.UserRequest;
import com.example.hospital.model.dto.respon.ApiDataRespon;
import com.example.hospital.model.dto.respon.UserRespon;
import com.example.hospital.model.enums.UserRole;
import com.example.hospital.model.enums.UserStatus;
import com.example.hospital.service.impl.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/admin/users")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl uImpl;

    // Xem danh sach + phan trang
    @GetMapping
    public ResponseEntity<ApiDataRespon<Page<UserRespon>>> findAll(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Page<UserRespon> users = uImpl.findAll(page, size, UserRole.PATIENT);
        ApiDataRespon<Page<UserRespon>> response = ApiDataRespon.<Page<UserRespon>>builder()
                .status(true).message("Lay danh sach thanh cong").data(users)
                .httpStatus(HttpStatus.OK).build();
        return ResponseEntity.ok(response);
    }

    // Xem chi tiet
    @GetMapping("/deltal/{id}")
    public ResponseEntity<ApiDataRespon<UserRespon>> findById(@PathVariable Long id) {
        UserRespon user = uImpl.findById(id);
        ApiDataRespon<UserRespon> response = ApiDataRespon.<UserRespon>builder().status(true)
                .message("Lay thong tin thanh cong").data(user).httpStatus(HttpStatus.OK).build();
        return ResponseEntity.ok(response);
    }

    // Sua
    @PutMapping("/update/{id}")
    public ResponseEntity<ApiDataRespon<Boolean>> update(@PathVariable Long id,
            @Valid @RequestBody UserRequest request) {
        Boolean isUpdated = uImpl.update(id, request);
        ApiDataRespon<Boolean> response = ApiDataRespon.<Boolean>builder().status(true)
                .message("Cap nhat nguoi dung thanh cong").data(isUpdated).httpStatus(HttpStatus.OK)
                .build();
        return ResponseEntity.ok(response);
    }

    // Xoa (soft delete)
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiDataRespon<UserStatus>> delete(@PathVariable Long id) {
        UserStatus status = uImpl.delete(id);
        ApiDataRespon<UserStatus> response = ApiDataRespon.<UserStatus>builder().status(true)
                .message("Xoa thanh cong").data(status).httpStatus(HttpStatus.OK).build();
        return ResponseEntity.ok(response);
    }
}
