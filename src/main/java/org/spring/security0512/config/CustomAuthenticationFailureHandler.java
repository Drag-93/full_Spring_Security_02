package org.spring.security0512.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.naming.AuthenticationException;
import java.io.IOException;

public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
//    @Override
//    public void onAuthenticationFailure(HttpServletRequest request,
//                                        HttpServletResponse response,
//                                        AuthenticationException exception) throws IOException, ServletException{
//
//        //인증이 실패시 상태
//        response.setStatus(HttpStatus.UNAUTHORIZED.value());
//        //기본적으로 전달하는 예외메세지를 작성
//        String errorMessage="에러메세지";
//        //시큐리트 로그인 예외가 발생이 되면 exception 별로 분류
//
//
//
//    }
}
