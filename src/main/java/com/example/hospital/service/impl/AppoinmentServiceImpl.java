package com.example.hospital.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.example.hospital.exception.ProductNotFoundException;
import com.example.hospital.mapper.AppoinmentMapper;
import com.example.hospital.model.dto.request.AppoinmentRequest;
import com.example.hospital.model.dto.respon.AppoinmentRespon;
import com.example.hospital.model.entity.Appoinment;
import com.example.hospital.model.entity.User;
import com.example.hospital.model.enums.AppoinmentStatus;
import com.example.hospital.repository.AppoinmentRepository;
import com.example.hospital.repository.UserRepository;
import com.example.hospital.service.AppoinmentService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppoinmentServiceImpl implements AppoinmentService {

    private final AppoinmentRepository aRepository;
    private final AppoinmentMapper aMapper;
    private final UserRepository uRepository;

    @Override
    public Page<AppoinmentRespon> findByPatient(Long patientId, Integer page, Integer pageSize) {
        // TODO Auto-generated method stub
        Pageable pageable = PageRequest.of(page, pageSize);
        return aRepository.findByPatientId(patientId, pageable).map(aMapper::toRespon);
    }

    @Override
    public Page<AppoinmentRespon> findByDoctor(Long doctorId, Integer page, Integer pageSize) {
        // TODO Auto-generated method stub
        Pageable pageable = PageRequest.of(page, pageSize);
        return aRepository.findByDoctorId(doctorId, pageable).map(aMapper::toRespon);
    }

    @Override
    public AppoinmentRespon findById(Long id) {
        return aMapper.toRespon(aRepository.findById(id).orElse(null));
    }

    @Override
    public Page<AppoinmentRespon> findAll(Integer page, Integer pageSize) {

        Pageable pageable = PageRequest.of(page, pageSize);
        return aRepository.findAll(pageable).map(aMapper::toRespon);
    }

    @Override
    public Boolean create(AppoinmentRequest request) {
        if (request == null)
            return false;
        User doctor = uRepository.findById(request.getDoctor_id()).orElse(null);
        User patient = uRepository.findById(request.getPatient_id()).orElse(null);
        Appoinment a = new Appoinment();
        a = aMapper.toEntity(request, a);
        a.setDoctor(doctor);
        a.setPatient(patient);
        aRepository.save(a);
        return true;

    }

    @Override
    public Boolean update(Long id, AppoinmentRequest request) {
        Appoinment a = aRepository.findById(id).orElse(null);
        if (a == null)
            return false;
        a = aMapper.toEntity(request, a);
        aRepository.save(a);
        return true;
    }

    @Override
    public Boolean delete(Long id) {

        Appoinment a = aRepository.findById(id).orElse(null);
        if (a == null)
            return false;
        aRepository.delete(a);
        return true;
    }

    @Override
    public AppoinmentStatus nextStatus(Long id) {
        Appoinment a = aRepository.findById(id).orElse(null);
        if (a.getStatus() == AppoinmentStatus.PENDING) {
            a.setStatus(AppoinmentStatus.APPROVED);
        } else if (a.getStatus() == AppoinmentStatus.APPROVED) {
            a.setStatus(AppoinmentStatus.REJECTED);
        } else if (a.getStatus() == AppoinmentStatus.REJECTED) {
            a.setStatus(AppoinmentStatus.COMPLETED);
        } else if (a.getStatus() == AppoinmentStatus.COMPLETED) {
            a.setStatus(AppoinmentStatus.CANCELLED);
        }
        aRepository.save(a);
        return a.getStatus();

    }

    @Override
    public AppoinmentStatus setStatus(Long id, AppoinmentStatus appoinmentStatus) {
        Appoinment a = aRepository.findById(id).orElse(null);
        a.setStatus(appoinmentStatus);
        aRepository.save(a);
        return a.getStatus();
    }

    @Override
    public Page<AppoinmentRespon> findByPatientId(Long id, Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return aRepository.findByPatientId(id, pageable).map(aMapper::toRespon);
    }

}
