package com.example.hospital.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.example.hospital.model.entity.User;
import com.example.hospital.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import com.example.hospital.model.dto.request.AppoinmentRequest;
import com.example.hospital.model.dto.respon.AppoinmentRespon;
import com.example.hospital.model.entity.Appoinment;


@Component
@RequiredArgsConstructor
public class AppoinmentMapper {
    private final UserRepository userRepository;

    public Appoinment toEntity(AppoinmentRequest ar, Appoinment a) {

        User doctor = userRepository.findById(ar.getDoctor_id()).orElse(null);
        User patient = userRepository.findById(ar.getPatient_id()).orElse(null);
        a.setStartTime(ar.getStartTime());
        a.setEndTime(ar.getEndTime());
        a.setSymptom(ar.getSymptom());
        a.setPatient(patient);
        a.setDoctor(doctor);
        return a;
    }

    public AppoinmentRespon toRespon(Appoinment a) {
        return AppoinmentRespon.builder().startTime(a.getStartTime()).endTime(a.getEndTime())
                .symptom(a.getSymptom()).diagnosis(a.getDiagnosis()).createAt(a.getCreateAt())
                .upDateAt(a.getUpDateAt()).doctor_id(a.getDoctor().getId()).patient_id(a.getPatient().getId()).build();
    }


    public List<AppoinmentRespon> toResponseList(Page<Appoinment> a) {
        return a.stream().map(this::toRespon).collect(Collectors.toList());
    }
}
