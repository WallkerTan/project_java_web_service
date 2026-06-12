package com.example.hospital.security;

import java.util.Collection;
import java.util.List;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.example.hospital.model.entity.User;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserDetail implements UserDetails {

    private final User user;

    // xác định quyền hạn user
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name().toString()));
    }

    @Override
    public @Nullable String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserName();
    }

}
