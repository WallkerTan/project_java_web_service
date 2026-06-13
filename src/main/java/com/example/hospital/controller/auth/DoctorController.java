package com.example.hospital.controller.auth;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.hospital.model.dto.request.UserRequest;
import com.example.hospital.model.dto.respon.ApiDataRespon;
import com.example.hospital.model.dto.respon.AppoinmentRespon;
import com.example.hospital.model.entity.User;
import com.example.hospital.model.enums.AppoinmentStatus;
import com.example.hospital.security.UserDetail;
import com.example.hospital.service.impl.AppoinmentServiceImpl;
import com.example.hospital.service.impl.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("hospital/doctor")
@RequiredArgsConstructor
public class DoctorController {

    private final UserServiceImpl uImpl;
    private final AppoinmentServiceImpl aImpl;

    // lấy ds lịch khám của mình
    @GetMapping("/appoinments")
    public ResponseEntity<ApiDataRespon<Page<AppoinmentRespon>>> getap(

            @AuthenticationPrincipal UserDetail userDetail,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {

        User u = uImpl.getUser(userDetail.getUsername());
        Page<AppoinmentRespon> list = aImpl.findByPatientId(u.getId(),page,size);
        return new ResponseEntity<>(
                new ApiDataRespon<>(true, "lay ds thanh cong", list, HttpStatus.OK), HttpStatus.OK);
    }

    // chỉnh sửa trạng thái , chuẩn đoán (cập nhật)
    @PostMapping("/diagnosis/{id}")
    public ResponseEntity<ApiDataRespon<AppoinmentStatus>> diagnosis(@PathVariable Long id) {
        return new ResponseEntity<>(
                new ApiDataRespon<>(true, "kham thanh cong", aImpl.nextStatus(id), HttpStatus.OK),
                HttpStatus.OK);
    }

}
