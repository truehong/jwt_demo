package com.sejin.jwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.web.filter.CorsFilter;

import com.sejin.jwt.filter.MyFilter1;
import com.sejin.jwt.filter.MyFilter3;
import com.sejin.jwt.jwt.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

/*
 *   1. jwt 방식에서는 session 을 이용한 로그인 방식을 쓰지 않는다.
 *      http.csrf().disable();
 *      http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
 *
 *   2. form 로그인 사용안함
 *      formLogin().disable() // jwt 서버니까 id, pw 로 폼로그인 안함
 *
 *   3. 기본적인 http 로그인 방식 사용하지 않는다
 *      httpBasic().disable()
 * */

/**
 *
 * formLogin().disable() 하면
 *.and()
 *.formLogin()
 *.loginProcessingUrl("/login") 동작 안함
 * 그래서 필터 생성함 -> jwtAuthenticationFilter
 *
 * */
@Configuration // ioc
@EnableWebSecurity // security 활성화
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final CorsFilter corsFilter;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.addFilterBefore(new MyFilter3(), SecurityContextPersistenceFilter.class);
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //session 사용않겠다. stateLess로 만들겠다.
        .and()
        .addFilter(corsFilter) // @CrossOrigin(인증x) , 시큐리티 필터에 등록 인증(o)
        .formLogin().disable() // jwt 서버니까 id, pw 로 폼로그인 안함
        .httpBasic().disable() // 이 라인까지 고정으로 걸어둔다.
        .addFilter(new JwtAuthenticationFilter(authenticationManager())) //AuthenticationManager 을 던져 져야함 -> 매니저를 통해 로그인을 진행하는 필터이기 때문에
        .authorizeRequests()
        .antMatchers("/api/v1/user/**")
        .access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER')  or hasRole('ROLE_ADMIN')")
        .antMatchers("/api/v1/manager/**")
        .access("hasRole('ROLE_MANAGER')  or hasRole('ROLE_ADMIN')")
        .antMatchers("/api/v1/admin/**")
        .access("hasRole('ROLE_ADMIN')") // 최고 권한
        .anyRequest().permitAll()
        .and()
        .formLogin()
        .loginProcessingUrl("/login"); // 다른 request 는 권한없이
    }
}
