package org.spring.security0512.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@Configuration//설정(공유)
@EnableWebSecurity // 시큐리티 <-사용자 로그인요청 처리
public class WebSecurityConfigClass {
    //비밀번호 암호화 등록 @Configuration
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception{
        //반드시 람다식 시큐리티 3.x
        //1. csrf 처리
//        http.csrf(AbstractHttpConfigurer::disable);
        http.csrf(cs->cs.disable());
        //2. 사용자 요청에 대한 페이지별 설정
        http.authorizeHttpRequests(
                authorize->
                        //모든 접근 허용
                        authorize.requestMatchers("/","member/login","/member/join","/member/loginFail").permitAll()
                                .requestMatchers("/css/**","/images/**","/js/**","/upload/**").permitAll()
                                .requestMatchers("/member/logout","/member/detail/**").authenticated()//로그인 후 접근
                                .requestMatchers("/admin/**").hasRole("ADMIN")//관리자 권한
                                .requestMatchers("/shop/**").hasAnyRole("ADMIN","MANAGER") //관리자, 매니저 권한
                                .anyRequest().permitAll()//나머지 접근 허용
        );
        //3. 로그인
        http.formLogin(login->
                        login.loginPage("/member/login")//새롭게 만든 로그인 페이지
                                .usernameParameter("userEmail")//로그인 아이디
                                .passwordParameter("userPw")//로그인 비밀번호
                                .loginProcessingUrl("/member/login")//form action -> POST, form 로그인 url
                                .defaultSuccessUrl("/")//로그인 설정시
//                        .failureUrl("/member/lgoinFail") //로그인 실패시
                                .failureUrl("/member/login?error=true") //로그인 실패시 -> 다시 로그인 페이지로
                                .permitAll()
        );
        //oAuth2방식 로그인
        http.oauth2Login(oauth2->oauth2
                .loginPage("/member/login")
                .userInfoEndpoint(userInfo->
                        userInfo.userService(myOAuth2UserService())));
        //4. 로그아웃
        http.logout(logout->
                logout.logoutUrl("/member/logout") // 로그아웃 URL기본/ logout
//                        .logoutSuccessUrl("/")//로그아웃 성공후 URL
                        .logoutSuccessHandler(customLogOutSuccessHandler())
                        .permitAll()
        );
        return http.build();
    }

    //OAuth 처리 구현체
    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User>myOAuth2UserService(){
        // MyDefaultOAuth2UserService 구현체가 반드시 존재해야 한다.
        return new MyDefaultOAuth2UserService();
    }



    //시큐리티 로그인 성공시
    @Bean // 빈으로 등록 ** 빈은 프로젝트 하나만 등록
    public CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler(){
        return new CustomAuthenticationSuccessHandler();
    }
    //시큐리티 로그인 실패시
    @Bean
    public CustomAuthenticationFailureHandler customAuthenticationFailureHandler(){
        return new CustomAuthenticationFailureHandler();
    }
    //시큐리티 로그아웃 성공시
    @Bean
    public CustomLogOutSuccessHandler customLogOutSuccessHandler(){
        return new CustomLogOutSuccessHandler();
    }
}

