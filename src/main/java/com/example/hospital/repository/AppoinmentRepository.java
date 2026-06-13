package com.example.hospital.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.hospital.model.dto.respon.AppoinmentRespon;
import com.example.hospital.model.entity.Appoinment;
import com.example.hospital.model.enums.UserStatus;

@Repository
public interface AppoinmentRepository extends JpaRepository<Appoinment,Long> {

    // Tìm theo bệnh nhân + phân trang
    Page<Appoinment> findByPatientId(Long patientId, Pageable pageable);

    //lấy hêt
    Page<Appoinment> findAll(Pageable pageable);

    //tìm theo id
    Optional<Appoinment> findById(Long id);


    // Tìm theo bác sĩ + phân trang
    Page<Appoinment> findByDoctorId(Long doctorId, Pageable pageable);

    // Tìm theo id (có sẵn findById từ JpaRepository rồi)
    // findAll(Pageable pageable) cũng có sẵn từ JpaRepository

    // Tìm theo bệnh nhân và bác sĩ (để kiểm tra trùng lịch)
    Page<Appoinment> findByPatientIdAndDoctorId(Long patientId, Long doctorId, Pageable pageable);

}
