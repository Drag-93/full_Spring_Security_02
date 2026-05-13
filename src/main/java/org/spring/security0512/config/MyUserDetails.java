package org.spring.security0512.config;

import lombok.Getter;
import lombok.Setter;
import org.spring.security0512.entity.MemberEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Getter
@Setter
public class MyUserDetails implements UserDetails , OAuth2User {
    //oAuth2 관리
    private Map<String,Object> getAttributes;

    private MemberEntity memberEntity;

    //기존 회원
    public MyUserDetails(MemberEntity memberEntity){
        System.out.println(memberEntity + "<<로그인 사용자");
        this.memberEntity=memberEntity;
    }

    //oAuth2 관리
    public MyUserDetails(MemberEntity memberEntity,Map<String,Object>getAttributes){
        this.memberEntity=memberEntity;
        this.getAttributes=getAttributes;
    }
    //oAuth2
    @Override
    public Map<String, Object> getAttributes() {
        return getAttributes;
    }
    //oAuth2
    @Override
    public String getName() {
        return memberEntity.getUserName();
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collectRoles=new ArrayList<>();
        collectRoles.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return "Role_"+memberEntity.getRole().toString();
            }
        });
        return collectRoles;
    }

    //커스텀해서 만들수 있다.
    public Long getId(){
        return memberEntity.getId();
    }
    @Override
    public String getPassword() {
        return memberEntity.getUserPw();
    }

    @Override
    public String getUsername() {
        return memberEntity.getUserEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}

