package com.example.hospital.mapper;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import com.example.hospital.model.dto.request.UserRequest;
import com.example.hospital.model.dto.respon.UserRespon;
import com.example.hospital.model.entity.User;


@Component
public class UserMapper {
    public User toEntity(UserRequest userRequest, User user) {
        user.setUserName(userRequest.getUserName());
        user.setEmail(userRequest.getEmail());
        user.setPassword(userRequest.getPassword());
        return user;
    }

    public UserRespon toRespon(User user) {
        if (user == null)
            return null;

        return UserRespon.builder().id(user.getId()).userName(user.getUsername())
                .email(user.getEmail()).password(user.getPassword()).createAt(user.getCreateAt())
                .updateAt(user.getUpdateAt()).role(user.getRole()).build();
    }

    public List<UserRespon> toResponseList(Page<User> users) {
        return users.stream().map(this::toRespon).collect(Collectors.toList());
    }
}
