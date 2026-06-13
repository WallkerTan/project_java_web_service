package com.example.hospital.controller.auth;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.hospital.model.dto.request.AppoinmentRequest;
import com.example.hospital.model.dto.request.UserRequest;
import com.example.hospital.model.dto.respon.ApiDataRespon;
import com.example.hospital.model.dto.respon.UserRespon;
import com.example.hospital.model.entity.User;
import com.example.hospital.model.enums.AppoinmentStatus;
import com.example.hospital.model.enums.UserRole;
import com.example.hospital.model.enums.UserStatus;
import com.example.hospital.service.impl.AppoinmentServiceImpl;
import com.example.hospital.service.impl.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("hospital/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserServiceImpl uImpl;
    private final AppoinmentServiceImpl aImpl;

    // quản lí bác sĩ
    // xem toàn bộ bác sĩ + phân trang
    @GetMapping("/doctors")
    public ResponseEntity<ApiDataRespon<Page<UserRespon>>> getDoctors(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {

        return new ResponseEntity<>(new ApiDataRespon<>(true, "lay danh sach bac si thnah cong",
                uImpl.findAll(page, size, UserRole.DOCTOR), HttpStatus.OK), HttpStatus.OK);
    }

    // xem chi tiết
    @GetMapping("doctors/deltail/{id}")
    public ResponseEntity<ApiDataRespon<UserRespon>> getDoctor(@PathVariable Long id) {
        return new ResponseEntity<>(new ApiDataRespon<>(true, "lay doctor" + id + " thanh cong",
                uImpl.findById(id), HttpStatus.OK), HttpStatus.OK);
    }

    // xóa
    @DeleteMapping("doctors/delete/{id}")
    public ResponseEntity<ApiDataRespon<UserStatus>> deleteDoctor(@PathVariable Long id) {
        return new ResponseEntity<>(new ApiDataRespon<>(true, "xoa thanh cong doctor",
                uImpl.delete(id), HttpStatus.NO_CONTENT), HttpStatus.NO_CONTENT);
    }

    // thêm doctor
    @PostMapping("/doctors/create")
    public ResponseEntity<ApiDataRespon<Boolean>> createDoctor(
            @Valid @RequestBody UserRequest userRequest) {
        return new ResponseEntity<>(new ApiDataRespon<>(true, "them bs thanh cong",
                uImpl.create(userRequest), HttpStatus.CREATED), HttpStatus.CREATED);
    }

    // duyet lí lịch khám
    // chuyển staus từ PENDING -> APPROCED
    @PutMapping("/Accepted/{id}")
    public ResponseEntity<ApiDataRespon<AppoinmentStatus>> updateStatusAppoinment(
            @PathVariable Long id) {
        return new ResponseEntity<>(
                new ApiDataRespon<>(true, "update thanh cong",
                        aImpl.setStatus(id, AppoinmentStatus.APPROVED), HttpStatus.OK),
                HttpStatus.OK);
    }

    // TỪ CHỐI LỊCH KHÁM STAUS = CANCELLED;
    @PutMapping("/Canceller/{id}")
    public ResponseEntity<ApiDataRespon<AppoinmentStatus>> CancellerAppoinment(
            @PathVariable Long id) {
        return new ResponseEntity<>(
                new ApiDataRespon<>(true, "huy lich kham thanh cong",
                        aImpl.setStatus(id, AppoinmentStatus.CANCELLED), HttpStatus.OK),
                HttpStatus.OK);
    }
}
