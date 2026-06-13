package com.example.hospital.controller.auth;

import com.example.hospital.mapper.UserMapper;
import com.example.hospital.model.dto.request.AppoinmentRequest;
import com.example.hospital.model.dto.respon.ApiDataRespon;
import com.example.hospital.model.dto.respon.AppoinmentRespon;
import com.example.hospital.model.entity.User;
import com.example.hospital.security.UserDetail;
import com.example.hospital.service.impl.AppoinmentServiceImpl;
import com.example.hospital.service.impl.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/patient/appoinments")
@RequiredArgsConstructor
public class AppoinmentController {

    private final AppoinmentServiceImpl aImpl;
    private final UserServiceImpl uImpl;
    private final UserMapper uMapper;

    // lấy toàn bộ lịch khám + phân trang
    @GetMapping
    public ResponseEntity<ApiDataRespon<Page<AppoinmentRespon>>> findAll(
            @AuthenticationPrincipal UserDetail userDetail,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {

        User u = uImpl.getUser(userDetail.getUsername());
        Page<AppoinmentRespon> list = aImpl.findByPatientId(u.getId(), page, size);
        ApiDataRespon<Page<AppoinmentRespon>> response = ApiDataRespon
                .<Page<AppoinmentRespon>>builder().status(true).message("Lay danh sach thanh cong")
                .data(list).httpStatus(HttpStatus.OK).build();
        return ResponseEntity.ok(response);
    }

    // thêm lịch hẹn
    @PostMapping("/create")
    public ResponseEntity<ApiDataRespon<Boolean>> save(
            @Valid @RequestBody AppoinmentRequest request) {
        return new ResponseEntity<>(new ApiDataRespon<>(true, "them lich hen thanh cong",
                aImpl.create(request), HttpStatus.OK), HttpStatus.OK);
    }

    // Xem chi tiet
    @GetMapping("/deltail/{id}")
    public ResponseEntity<ApiDataRespon<AppoinmentRespon>> findById(@PathVariable Long id) {
        AppoinmentRespon appoinment = aImpl.findById(id);
        ApiDataRespon<AppoinmentRespon> response = ApiDataRespon.<AppoinmentRespon>builder()
                .status(true).message("Lay thong tin thanh cong").data(appoinment)
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
