package org.spring.security0512;

import org.junit.jupiter.api.Test;
import org.spring.security0512.dto.MemberDto;
import org.spring.security0512.entity.MemberEntity;
import org.spring.security0512.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class MemberTest {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void memberInsert() {
        for (int i = 1; i <= 100; i++) {
            MemberDto memberDto = new MemberDto();
            memberDto.setUserEmail("m" + i + "@email.com");
            memberDto.setUserPw("11");
            memberDto.setUserName("m" + i);

            memberRepository.save(MemberEntity.toInsertMemberEntity(memberDto,passwordEncoder));
        }
    }
}