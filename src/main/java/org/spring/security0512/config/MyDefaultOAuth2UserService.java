package org.spring.security0512.config;

import org.spring.security0512.common.Role;
import org.spring.security0512.entity.MemberEntity;
import org.spring.security0512.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class MyDefaultOAuth2UserService extends DefaultOAuth2UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MemberRepository memberRepository;
    //OAuth -> 사용자
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException{
        OAuth2User oAuth2User = super.loadUser(userRequest);
        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        String clientId = clientRegistration.getClientId();
        String registrationId=clientRegistration.getRegistrationId();
        Map<String,Object> attributes=oAuth2User.getAttributes();
        return oAuth2UserSuccess(oAuth2User, registrationId);
    }

    private OAuth2User oAuth2UserSuccess(OAuth2User oAuth2User, String registrationId) {
        String userEmail="";
        String userName="";
        String userPw="";

        if(registrationId.equals("google")){
            userEmail=oAuth2User.getAttribute("email");
            userName=oAuth2User.getAttribute("name");
        }else if(registrationId.equals("kakao")){

        }else if(registrationId.equals("naver")){
            System.out.println("네이버 로그인");
            Map<String,Object> response=(Map<String,Object>) oAuth2User.getAttributes().get("response");

            userEmail=(String)response.get("email");
            userName=(String)response.get("name");

            System.out.println(response);
            System.out.println((String)response.get("id"));
            System.out.println((String)response.get("nickname"));
            System.out.println((String)response.get("gender"));
            System.out.println((String)response.get("email"));
            System.out.println((String)response.get("name"));
        }
        Optional<MemberEntity> optionalMemberEntity=memberRepository.findByUserEmail(userEmail);

        //등록 되어 있으면
        if(optionalMemberEntity.isPresent()){
            //기존 회원 관리
            return new MyUserDetails(optionalMemberEntity.get());
        }
        //처음 SNS회원 등록
        //비밀번호 암호화
        userPw=passwordEncoder.encode("11");

        MemberEntity memberEntity=memberRepository.save(
                MemberEntity.builder()
                        .userEmail(userEmail)
                        .userPw(userPw)
                        .userName(userName)
                        .role(Role.MEMBER).build());
                return new MyUserDetails(memberEntity, oAuth2User.getAttributes());
    }
}
