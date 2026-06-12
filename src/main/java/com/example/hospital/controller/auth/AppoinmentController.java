package com.example.hospital.controller.auth;

import com.example.hospital.model.dto.request.AppoinmentRequest;
import com.example.hospital.model.dto.respon.ApiDataRespon;
import com.example.hospital.model.dto.respon.AppoinmentRespon;
import com.example.hospital.service.impl.AppoinmentServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("hospital/appoinment")
@RequiredArgsConstructor
public class AppoinmentController {

    private final AppoinmentServiceImpl aImpl;

    // Xem danh sach + phan trang
    @GetMapping
    public ResponseEntity<ApiDataRespon<Page<AppoinmentRespon>>> findAll(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Page<AppoinmentRespon> list = aImpl.findAll(page, size);
        ApiDataRespon<Page<AppoinmentRespon>> response = ApiDataRespon
                .<Page<AppoinmentRespon>>builder().status(true).message("Lay danh sach thanh cong")
                .data(list).httpStatus(HttpStatus.OK).build();
        return ResponseEntity.ok(response);
    }

    //thêm lịch hẹn
    @PostMapping
    public ResponseEntity<ApiDataRespon<Boolean>> save(@Valid @RequestBody AppoinmentRequest request) {
        return new ResponseEntity<>(new ApiDataRespon<>(
                true,
                "them lich hen thanh cong",
                aImpl.create(request),
                HttpStatus.OK
        ),HttpStatus.OK);
    }

    // Xem chi tiet
    @GetMapping("/{id}")
    public ResponseEntity<ApiDataRespon<AppoinmentRespon>> findById(@PathVariable Long id) {
        AppoinmentRespon appoinment = aImpl.findById(id);
        ApiDataRespon<AppoinmentRespon> response = ApiDataRespon.<AppoinmentRespon>builder()
                .status(true).message("Lay thong tin thanh cong").data(appoinment)
                .httpStatus(HttpStatus.OK).build();
        return ResponseEntity.ok(response);
    }

    // Tim theo benh nhan
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<ApiDataRespon<Page<AppoinmentRespon>>> findByPatient(
            @PathVariable Long patientId, @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Page<AppoinmentRespon> list = aImpl.findByPatient(patientId, page, size);
        ApiDataRespon<Page<AppoinmentRespon>> response =
                ApiDataRespon.<Page<AppoinmentRespon>>builder().status(true)
                        .message("Lay lich hen theo benh nhan thanh cong").data(list)
                        .httpStatus(HttpStatus.OK).build();
        return ResponseEntity.ok(response);
    }

    // Tim theo bac si
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<ApiDataRespon<Page<AppoinmentRespon>>> findByDoctor(
            @PathVariable Long doctorId, @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Page<AppoinmentRespon> list = aImpl.findByDoctor(doctorId, page, size);
        ApiDataRespon<Page<AppoinmentRespon>> response =
                ApiDataRespon.<Page<AppoinmentRespon>>builder().status(true)
                        .message("Lay lich hen theo bac si thanh cong").data(list)
                        .httpStatus(HttpStatus.OK).build();
        return ResponseEntity.ok(response);
    }

    // Sua lich hen -> Boolean
    @PutMapping("/{id}")
    public ResponseEntity<ApiDataRespon<Boolean>> update(@PathVariable Long id,
            @Valid @RequestBody AppoinmentRequest request) {
        Boolean isUpdated = aImpl.update(id, request);
        ApiDataRespon<Boolean> response = ApiDataRespon.<Boolean>builder().status(true)
                .message("Cap nhat lich hen thanh cong").data(isUpdated).httpStatus(HttpStatus.OK)
                .build();
        return ResponseEntity.ok(response);
    }

    // Xoa lich hen
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiDataRespon<Void>> delete(@PathVariable Long id) {
        aImpl.delete(id);
        ApiDataRespon<Void> response = ApiDataRespon.<Void>builder().status(true)
                .message("Xoa lich hen thanh cong").data(null).httpStatus(HttpStatus.OK).build();
        return ResponseEntity.ok(response);
    }
}
