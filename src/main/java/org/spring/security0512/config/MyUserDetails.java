package org.spring.security0512.config;

import lombok.Getter;
import lombok.Setter;
import org.spring.security0512.entity.MemberEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
public class MyUserDetails implements UserDetails {
    private MemberEntity memberEntity; // 로그인 사용자

    public MyUserDetails(MemberEntity memberEntity){
        System.out.println(memberEntity);
        this.memberEntity=memberEntity;
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

