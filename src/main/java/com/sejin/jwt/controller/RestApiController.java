package com.sejin.jwt.controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sejin.jwt.model.User;
import com.sejin.jwt.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class RestApiController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/home")
    public String home() {
        return "<h1>Home</h1>";
    }

    @PostMapping("/token")
    public String token() {
        return "<h1>token</h1>";
    }

    @PostMapping("/join")
    public String join(@RequestBody User user){
        user.setPassword(passwordEncoder.encode(user.getPassword())); // 1234ppp -> ABC3333
        user.setRole("ROLE_USER");
        userRepository.save(user);
        return "회원가입 완료";
    }

}
