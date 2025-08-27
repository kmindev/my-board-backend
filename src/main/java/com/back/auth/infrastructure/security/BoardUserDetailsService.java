package com.back.auth.infrastructure.security;

import com.back.user.infrastructure.repository.UserAccountRepository;
import com.back.auth.domain.BoardUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BoardUserDetailsService implements UserDetailsService {

    private final UserAccountRepository userAccountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userAccountRepository.findById(username)
                .map(BoardUserDetails::from)
                .orElseThrow(() -> new UsernameNotFoundException("ID 가 존재하지 않습니다."));
    }

}
