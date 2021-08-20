package com.sejin.jwt.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyFilter3 implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws
            IOException,
            ServletException {

        System.out.println("------------------필터3------------------");
        HttpServletRequest req = (HttpServletRequest) request; //downCasting
        HttpServletResponse res = (HttpServletResponse) response;
        res.setCharacterEncoding("UTF-8");

        /**
         * 토큰을 만들었다고 가정 : 코스 -
         * cos 제대로 들어오면 인증된거니까 하고 아니면 인증안됨처리
         * 가장 먼저 되어야 하니까 security 에 직접 MyFilter1 집어넣기
         * id, pw 정상적으로 들어와서 로그인이 완료 되면 토큰을 만들어 주고 그걸로 응답을 해준다.
         * 요청 할때마다 header 에 Authorization 에 value 값으로 토큰을 가져오는데
         * 그때 토큰이 넘어오면 이 토큰이 내가 만든 토큰이 맞는지만 검증 하면 됨 (RSA, HS256)
         * */
        if(req.getMethod().equals("POST")) {
            System.out.println("포스트 요청됨");
            String headerAuth = ((HttpServletRequest)request).getHeader("Authorization");
            System.out.println(headerAuth);
            if(headerAuth.equals("cos")){
                chain.doFilter(request, response);
            }else {
                PrintWriter printWriter = res.getWriter();
                printWriter.println("인증안됨");
            }
        }
    }
}
