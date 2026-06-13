package com.example.hospital.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.example.hospital.mapper.UserMapper;
import com.example.hospital.model.dto.request.UserRequest;
import com.example.hospital.model.dto.respon.UserRespon;
import com.example.hospital.model.entity.User;
import com.example.hospital.model.enums.UserRole;
import com.example.hospital.model.enums.UserStatus;
import com.example.hospital.repository.UserRepository;
import com.example.hospital.service.UserService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository uRepository;
    private final UserMapper uMapper;

    @Override
    public UserRespon findById(Long id) {
        return uMapper.toRespon(uRepository.findById(id).orElse(null));
    }

    @Override
    public UserRespon findByUsername(String username, UserRole role) {
        return uMapper.toRespon(uRepository.findByUserNameAndRole(username, role).orElse(null));
    }
    public User getUser(String Usernaem){
        return uRepository.findByUserName(Usernaem).orElse(null);
    }
    @Override
    public Page<UserRespon> findAll(Integer page, Integer size, UserRole role) {
        Pageable pageable = PageRequest.of(page, size);
        return uRepository.findByStatusAndRole(UserStatus.ACTIVE, pageable, role).map(uMapper::toRespon);
    }

    @Override
    public Boolean create(UserRequest request) {
        if (request == null)
            return false;
        return uRepository.save(uMapper.toEntity(request, new User())) != null;
    }

    @Override
    public Boolean update(Long id, UserRequest request) {
        if (request == null) {
            return false;
        }
        User u = uRepository.findById(id).orElse(null);
        if (u == null) {
            return false;
        }
        u = uMapper.toEntity(request, u);
        uRepository.save(u);
        return true;
    }

    @Override
    public UserStatus delete(Long id) {

        User u = uRepository.findById(id).orElse(null);
        if (u == null) {
            return null;
        }
        if (u.getStatus() != UserStatus.INACTIVE) {
            u.setStatus(UserStatus.INACTIVE);
        }
        uRepository.save(u);
        return u.getStatus();
    }

    @Override
    public Page<UserRespon> search(String keyword, Integer page, Integer pageSize, UserRole role) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return uRepository.searchByKeyword(keyword, pageable).map(uMapper::toRespon);
    }

    @Override
    public Boolean createDoctor(UserRequest request) {
        if (request == null)
            return false;
        User u = new User();
        u.setRole(UserRole.DOCTOR);
        return uRepository.save(uMapper.toEntity(request, u)) != null;
    }

    @Override
    public Boolean createAdmin(UserRequest request) {
        if (request == null)
            return false;
        User u = new User();
        u.setRole(UserRole.ADMIN);
        return uRepository.save(uMapper.toEntity(request, u)) != null;
    }

}
