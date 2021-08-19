package com.sejin.jwt.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.filter.CorsFilter;

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
@Configuration // ioc
@EnableWebSecurity // security 활성화
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final CorsFilter corsFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //session 사용않겠다. stateLess로 만들겠다.
        .and()
        .addFilter(corsFilter)
        .formLogin().disable() // jwt 서버니까 id, pw 로 폼로그인 안함
        .httpBasic().disable() // 이 라인까지 고정으로 걸어둔다.
        .authorizeRequests()
        .antMatchers("/api/v1/user/**")
        .access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER')  or hasRole('ROLE_ADMIN')")
        .antMatchers("/api/v1/manager/**")
        .access("hasRole('ROLE_MANAGER')  or hasRole('ROLE_ADMIN')")
        .antMatchers("/api/v1/admin/**")
        .access("hasRole('ROLE_ADMIN')") // 최고 권한
        .anyRequest().permitAll(); // 다른 request 는 권한없이
    }
}
