package com.sejin.jwt.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sejin.jwt.filter.MyFilter1;
import com.sejin.jwt.filter.MyFilter2;

/**
 * security 필터에 말고 내가 직접 만들기
 * custom filter
 * securityConfig 에서 addFilterBefore 에서 구현된 필터가 먼저 실행된다.
 * */
@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean<MyFilter1> filter1(){
        FilterRegistrationBean<MyFilter1> bean = new FilterRegistrationBean<>(new MyFilter1());
        bean.addUrlPatterns("/*");
        bean.setOrder(0); // 낮은 번호가 필터중에서 가장 먼저 실행 된다.
        return bean;
    }

    @Bean
    public FilterRegistrationBean<MyFilter2> filter2(){
        FilterRegistrationBean<MyFilter2> bean = new FilterRegistrationBean<>(new MyFilter2());
        bean.addUrlPatterns("/*");
        bean.setOrder(1); // 낮은 번호가 필터중에서 가장 먼저 실행 된다.
        return bean;
    }
}
