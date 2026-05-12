package org.spring.security0512.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.spring.security0512.common.Role;
import org.spring.security0512.entity.MemberEntity;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDto {
    private Long id;

    @NotBlank(message = "이메일을 입력해주세요")
    @Size(min = 3)
    private String userEmail;

    @NotBlank(message = "비빌번호를 입력해주세요")
    @Size(min = 2)
    private String userPw;

    @NotBlank(message = "이름을 입력해주세요")
    @Size(min = 1,max = 20)
    private String userName;

    private Role role;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    //Entity-> Dto
    public static MemberDto toMemberDto(MemberEntity memberEntity){
        MemberDto memberDto=new MemberDto();
        memberDto.setId(memberEntity.getId());
        memberDto.setUserEmail(memberEntity.getUserEmail());
        memberDto.setUserPw(memberEntity.getUserPw());
        memberDto.setUserName(memberEntity.getUserName());
        memberDto.setRole(memberEntity.getRole());
        memberDto.setCreateTime(memberEntity.getCreateTime());
        memberDto.setUpdateTime(memberEntity.getUpdateTime());
        return memberDto;
    }



}
