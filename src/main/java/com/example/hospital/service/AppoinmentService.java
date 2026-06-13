package com.example.hospital.service;

import org.springframework.data.domain.Page;
import com.example.hospital.model.dto.request.AppoinmentRequest;
import com.example.hospital.model.dto.respon.AppoinmentRespon;
import com.example.hospital.model.entity.Appoinment;
import com.example.hospital.model.enums.AppoinmentStatus;

public interface AppoinmentService {

    // Tìm theo bệnh nhân + phân trang
    Page<AppoinmentRespon> findByPatient(Long patientId, Integer page, Integer pageSize);

    // Tìm theo bác sĩ + phân trang
    Page<AppoinmentRespon> findByDoctor(Long doctorId, Integer page, Integer pageSize);

    // Lấy theo id
    AppoinmentRespon findById(Long id);


    Page<AppoinmentRespon> findByPatientId(Long id, Integer page, Integer pageSize);

    // Lấy hết + phân trang
    Page<AppoinmentRespon> findAll(Integer page, Integer pageSize);

    // Thêm
    Boolean create(AppoinmentRequest request);

    // Sửa (cập nhật diagnosis, fileUrl, status...)
    Boolean update(Long id, AppoinmentRequest request);

    // nextStatus
    AppoinmentStatus nextStatus(Long id);

    // setStatus
    AppoinmentStatus setStatus(Long id, AppoinmentStatus appoinmentStatus);

    // Xóa (đổi status = CANCELLED)
    Boolean delete(Long id);
}
