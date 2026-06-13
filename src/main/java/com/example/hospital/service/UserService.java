package com.example.hospital.service;

import org.springframework.boot.data.autoconfigure.web.DataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import com.example.hospital.model.dto.request.UserRequest;
import com.example.hospital.model.dto.respon.UserRespon;
import com.example.hospital.model.entity.User;
import com.example.hospital.model.enums.UserRole;
import com.example.hospital.model.enums.UserStatus;

public interface UserService {

    // Lấy theo id
    UserRespon findById(Long id);

    // Lấy theo tên
    UserRespon findByUsername(String username, UserRole role);

    // Lấy tất cả + phân trang
    Page<UserRespon> findAll(Integer page, Integer pageSize, UserRole role);

    // Thêm bệnh nhân
    Boolean create(UserRequest request);

    // Thêm bs
    Boolean createDoctor(UserRequest request);

    // Thêm admin
    Boolean createAdmin(UserRequest request);

    // Sửa
    Boolean update(Long id, UserRequest request);

    // Xóa (soft delete — đổi status = INACTIVE)
    UserStatus delete(Long id);

    // Tìm kiếm theo tên + phân trang
    Page<UserRespon> search(String keyword, Integer page, Integer pageSize, UserRole role);
}
