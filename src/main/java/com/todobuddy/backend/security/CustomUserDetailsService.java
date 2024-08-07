package com.todobuddy.backend.security;

import com.todobuddy.backend.entity.User;
import com.todobuddy.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User findUser = userRepository.findByEmail(username)
            .orElseThrow(
                () -> new UsernameNotFoundException("사용자를 찾을 수 없습니다. username=" + username));

        if (findUser == null) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다. username=" + username);
        }

        return new CustomUserDetails(findUser);
    }
}
