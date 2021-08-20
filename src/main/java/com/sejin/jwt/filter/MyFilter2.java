package com.sejin.jwt.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class MyFilter2 implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws
            IOException,
            ServletException {
        System.out.println("------------------필터2------------------");
        chain.doFilter(request, response); // 계속 프로세스를 진행해라
        // PrintWriter out = response.getWriter();
        // out.println("안녕");
    }
}
