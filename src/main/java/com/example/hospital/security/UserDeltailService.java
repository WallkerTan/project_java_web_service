package com.example.hospital.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.hospital.model.entity.User;
import com.example.hospital.repository.UserRepository;
import lombok.RequiredArgsConstructor;


// lấy user từ service
@Service
@RequiredArgsConstructor
public class UserDeltailService implements UserDetailsService {
    private final UserRepository uRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User u = uRepository.findByUserName(username).orElse(null);
        return new UserDetail(u);

    }

}
