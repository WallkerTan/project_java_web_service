package com.example.hospital.service;

import org.springframework.data.domain.Page;
import com.example.hospital.model.dto.request.AppoinmentRequest;
import com.example.hospital.model.dto.respon.AppoinmentRespon;

public interface AppoinmentService {

    // Tìm theo bệnh nhân + phân trang
    Page<AppoinmentRespon> findByPatient(Long patientId, Integer page, Integer pageSize);

    // Tìm theo bác sĩ + phân trang
    Page<AppoinmentRespon> findByDoctor(Long doctorId, Integer page, Integer pageSize);

    // Lấy theo id
    AppoinmentRespon findById(Long id);

    // Lấy hết + phân trang
    Page<AppoinmentRespon> findAll(Integer page, Integer pageSize);

    // Thêm
    Boolean create(AppoinmentRequest request);

    // Sửa (cập nhật diagnosis, fileUrl, status...)
    Boolean update(Long id, AppoinmentRequest request);

    // Xóa (đổi status = CANCELLED)
    Boolean delete(Long id);
}
