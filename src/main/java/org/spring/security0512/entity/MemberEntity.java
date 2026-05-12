package org.spring.security0512.entity;

import jakarta.persistence.*;
import lombok.*;
import org.spring.security0512.common.BasicTime;
import org.spring.security0512.common.Role;
import org.spring.security0512.dto.MemberDto;
import org.springframework.security.crypto.password.PasswordEncoder;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name= "sec_member_tb2")
public class MemberEntity extends BasicTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="member_id")
    private Long id;

    @Column(unique = true,nullable = false)
    private String userEmail;

    @Column(nullable = false)
    private String userPw;

    @Column(nullable = false)
    private String userName;

    @Enumerated(EnumType.STRING)
    private Role role;



    //회원가입
    public static MemberEntity toInsertMemberEntity(MemberDto memberDto, PasswordEncoder passwordEncoder){
        MemberEntity memberEntity=new MemberEntity();

        memberEntity.setUserEmail(memberDto.getUserEmail());
        //*******반드시 비밀번호 암호화
        //memberEntity.setUserPw(memberDto.getUserPw()); // <- 암호화 후 추가
        memberEntity.setUserPw(passwordEncoder.encode(memberDto.getUserPw()));
        memberEntity.setRole(Role.MEMBER);
        memberEntity.setUserName(memberDto.getUserName());
        return memberEntity;
    }


    public static MemberEntity toUpdateMemberEntity(MemberDto memberDto) {
        MemberEntity memberEntity=new MemberEntity();
        memberEntity.setId(memberDto.getId());
        memberEntity.setUserEmail(memberDto.getUserEmail());
        //*******반드시 비밀번호 암호화
        //memberEntity.setUserPw(memberDto.getUserPw()); // <- 암호화 후 추가
        memberEntity.setUserPw(memberDto.getUserPw());
        memberEntity.setRole(memberDto.getRole());
        memberEntity.setUserName(memberDto.getUserName());
        return memberEntity;
    }
}
