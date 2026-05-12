package org.spring.security0512.service;

import org.spring.security0512.dto.MemberDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MemberService {

    void memberInsert(MemberDto memberDto);

    MemberDto memberDetail(Long id);

    boolean emailCheck(String userEmail);

    List<MemberDto> memberList();

    void memberUpdate(MemberDto memberDto);

    void memberDelete(Long id);

    void memberDeleteByUserEmailAndUserPw(MemberDto memberDto);

    Page<MemberDto> pagingSearchListAll(Pageable pageable, String subject, String search);
}

