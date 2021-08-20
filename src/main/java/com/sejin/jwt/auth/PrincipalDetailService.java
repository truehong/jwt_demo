package com.sejin.jwt.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sejin.jwt.model.User;
import com.sejin.jwt.repository.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * 이 서비스 언제 동작 하냐?
 * http://localhost:8080/login 요청을 할때
 * 이유는 스프링 씨큐리티의 기본적인 로그인 요청 주소가 /login
 * */
@Service
@RequiredArgsConstructor
public class PrincipalDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("PrincipalDetailService.loadUserByUsername");
        User userEntity = userRepository.findByUsername(username);
        System.out.println("userEntity : " + userEntity);
        return new PrincipalDetails(userEntity);
    }
}
