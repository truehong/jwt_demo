package com.sejin.jwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * cors 정책에서 벗어나 이 filter 를 탈수 있음 (cross origin 요청이 와도)
 * */
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter () {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/api/**", config);
        return new CorsFilter(source);
    }
}


/*
*
* 1. config.setAllowCredentials(true);
*  내 서버가 응답할때 json 을 자바스크립트에서 처리할 수 있게 할지를 설정한다.
*  javascrpt 에서 ajax로 patch 나 axios 같은 라이브러리로 데이터를 요청하면 패치하는데 자바스크립트가 받아올수 있게 하는지.
*  false 로 하면 javascript 로 요청을 할때 응답이 오지 않는다.
* 2. addAllowedOrigin 모든 ip 의 응답 허용
* 3. addAllowedHeader 모든 header 에 응답 허용
* 4. addAllowedMethod 모든 method 에 응답 허용
* 5. 모든 http method(post, get, update , delete) 에 요청을 허용
* 이 config 를 필터에 등록 해야한다.
* */