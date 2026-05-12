package org.spring.security0512.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.io.PrintWriter;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        FilterChain chain,
                                        Authentication authentication)
            throws IOException, ServletException {

        System.out.println(authentication); //로그인 인증 객체
        System.out.println(authentication.getPrincipal()+"Principal"); // 로그인 사용자
        System.out.println(authentication.getPrincipal().toString());
        System.out.println(authentication.getAuthorities()); // 사용자 권한

        response.setContentType("text/html; charset=utf-8");
        PrintWriter out = response.getWriter();

        out.println("<script>alert('"+authentication.getName()+"님 반갑습니다')");
        out.close();

    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

    }
}
