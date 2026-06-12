package com.example.hospital.controller.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.hospital.model.dto.request.UserRequest;
import com.example.hospital.model.dto.respon.ApiDataRespon;
import com.example.hospital.service.impl.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("hospital/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserServiceImpl uImpl;
    //thêm bs
    //duyet lich kham
    //xoa bs
}
