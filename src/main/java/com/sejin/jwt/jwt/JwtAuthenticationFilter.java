package com.sejin.jwt.jwt;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.omg.CORBA.portable.ResponseHandler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sejin.jwt.auth.PrincipalDetails;
import com.sejin.jwt.model.User;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

/**
 * Spring security 에서 UsernamePasswordAuthenticationFilter
 * 내가 /login 이라고 요청해서 username , password 를 post로 전송하면 동작하는 필터
 * security config 에서 formLogin 을 disable 하면 동작 안한다.
 * 동작하기 위한 방법으로 filter를 security config 에 재등록
 * .addFilter() 해서 달아준다.
 * */
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    // login 요청을 하면 로그인 시도를 위해 실행되는 함수
    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws
            AuthenticationException {
        System.out.println("jwtAuthenticationFilter : 로그인 시도중");
        /*
         * 1. username, password 받아서
         * 2. 정상인지 로그인 시도 해본다. authenticationManager 로 로그인 시도를 하면 PrincipalDetailsService 가 호출 된다.
         * 그러면 loadUserByUsername() 이 자동실행 된다.
         * 3. principalDetails 가 세션에 담긴다. (SESSION 에 담는 이유는 권환 관리를 위해서)
         * 4. JWT 토큰을 만들어서 응답해주면 됨.
         * */
        //1) byte code 를 읽어 들여
        //BufferedReader br = request.getReader();

        // String input = null;
        // while ((input = br.readLine()) != null) {
        //     System.out.println(input);
        // }

        //JSON 파싱 객체
        ObjectMapper om = new ObjectMapper();
        User user = om.readValue(request.getInputStream(), User.class);
        System.out.println(user);

        //2)  파싱하기 &로
        System.out.println("======================================");

        //토큰 만들고
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        // 1.토큰 날리고 authentication 실행될때 principalDetailsService에 loadUserByUsername() 함수가 실행됨
        // 2. PrincipalDetailService의 loadUserByUsername() 함수가 실행된 후 정상이면 authentication이 리턴됨.
        // 3. authentication이이 리턴된다는건 DB에 있는 usename과 password 가 리턴 되었다는거
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        // 인증되면 authentication 객체를 받고 여기에는 나의 로그인 정보가 담긴다. 인증 완료되면 authentication 객체가 session영역에 저장이 된다.
        PrincipalDetails principalDetails = (PrincipalDetails)authentication.getPrincipal();
        System.out.println("----------------------------------------");
        System.out.println("로그인 완료됨: " + principalDetails.getUser().getUsername()); // 4. 출력이 되면 로그인 되었다는것
        System.out.println("1----------------------------------------");
        return authentication;      // 5. authentication 객체가 session 영역에 저장이 되어야 하고 그 방법이 return 해주는 것이다.
        // return 의 이유는 권한 관리를 security 가 대신 해주기 때문에 편하려고 하는것이다.
        // 굳이 jwt 토큰을 사용하면서 세션을 만들 이유가 없음, 근데 단지 권한 처리 때문에 session 을 넣어 준다.
    }

    // attemptAuthenticaton 실행 후 인증이 정상적으로 되었으면 successfulAuthentication 가 실행된다.
    // 이곳에서 JWT 토큰을 만들어서 request 요청한 사용자에게 jwt 토큰을 response 해주면 된다.
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
        System.out.println("JwtAuthenticationFilter.successfulAuthentication : 인증 완료됨");
        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal(); // 이아이를 받아서 jwt 를 받을거고, 라이브러리를 받을거에요.
        /**
         * jwt Token 은 기본적으로 builder 패턴
         * RSA 방식은 아니고 Hash 암호화 방식
         * */
       String jwtToken = JWT.create()
                .withSubject("cos token") // jwt token properties 만들기, 60000 1분, JwtProperty.SECRET
                .withExpiresAt(new Date(System.currentTimeMillis()+ (60000*10)))
                .withClaim("id", principalDetails.getUser().getId()) // 여기서 withClaim 이라는 것은 비공개 클레임인데 내가 넣고 싶은 key, value 막 넣으면 된다.
                .withClaim("username", principalDetails.getUser().getUsername())
                .sign(Algorithm.HMAC512("cos"));

        response.addHeader("Authorization", "Bearer" + jwtToken);

        super.successfulAuthentication(request, response, chain, authResult);
    }
}
