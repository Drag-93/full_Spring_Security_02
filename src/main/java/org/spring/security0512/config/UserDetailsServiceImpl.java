package org.spring.security0512.config;

import lombok.RequiredArgsConstructor;
import org.spring.security0512.entity.MemberEntity;
import org.spring.security0512.repository.MemberRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        System.out.println(userEmail+"<<userEmail");
        MemberEntity memberEntity = memberRepository.findByUserEmail(userEmail)
                .orElseThrow(()->new UsernameNotFoundException("이메일이 존재하지 않습니다."));
        System.out.println(memberEntity+"<<memberEntity");
        //User -> 사용자의 핵심 정보(이름, 권한, 상태)를 담는 객체
//        return User.builder()
//                .username(memberEntity.getUserEmail()) // 로그인 아이디(userEmail
//                .password(memberEntity.getUserPw()) // 로그인 비밀번호(usePw)
//                .roles(memberEntity.getRole().toString()) // 로그인 권한
//                .build();
        //이메일이 존재하면 -> 로그인    성공->
        return new MyUserDetails(memberEntity); //(커스텀)

    }
}
