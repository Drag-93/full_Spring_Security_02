package org.spring.security0512.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.spring.security0512.dto.MemberDto;
import org.spring.security0512.entity.MemberEntity;
import org.spring.security0512.repository.MemberRepository;
import org.spring.security0512.service.MemberService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public void memberInsert(MemberDto memberDto) {
        //이메일 체크
        memberRepository.findByUserEmail(memberDto.getUserEmail())
                //이메일이 존재하면
                .ifPresent(user->{throw new IllegalArgumentException("회원가입 불가"+memberDto.getUserEmail());});

        //회원가입 실행 -> 비밀번호 암호화
        memberRepository.save(MemberEntity.toInsertMemberEntity(memberDto,passwordEncoder));
    }

    @Override
    public MemberDto memberDetail(Long id) {
        Optional<MemberEntity> optionalMemberEntity=  memberRepository.findById(id);
        if(optionalMemberEntity.isEmpty()){
            throw  new NoSuchElementException("회원이 존재하지 않습니다.");
        }
        MemberEntity memberEntity=optionalMemberEntity.get();

        return MemberDto.toMemberDto(memberEntity);
    }

    @Override
    public boolean emailCheck(String userEmail) {
        return false;
    }

    @Override
    public List<MemberDto> memberList() {
        List<MemberDto> memberDtos = memberRepository.findAll().stream()
                .map(MemberDto::toMemberDto)
                .collect(Collectors.toList());

        return memberDtos;
    }

    @Transactional
    @Override
    public void memberUpdate(MemberDto memberDto) {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findById(memberDto.getId());
        if (!optionalMemberEntity.isPresent()) {
//            throw new NoSuchElementException("조회할 아이디가 없습니다");
            throw new NoSuchElementException("회원이 존재하지 않습니다.");
        }

        String oldUserEmail = optionalMemberEntity.get().getUserEmail();//이전이메일
        String newUserEmail = memberDto.getUserEmail();
        //이전이메일 하고 바뀐이메일이 다르면 -> 이메일 체크
        if (!oldUserEmail.equals(newUserEmail)) {
            //이전이메일 하고 바뀐이메일이 다르면 -> 이메일 체크
            if (memberRepository.existsByUserEmail(newUserEmail)) {
                throw new IllegalStateException("이메일이 이미존재합니다.");
            }
        }
        //기존 비빌번호 사용
        if(memberDto.getUserPw()==null){
            memberDto.setUserPw(optionalMemberEntity.get().getUserPw());
            System.out.println(optionalMemberEntity.get().getUserPw());
        }else{
            //새 비밀번호일 경우 반드시 비밀번호 암호화 해야된다.
            String userPw=passwordEncoder.encode(memberDto.getUserPw());
            memberDto.setUserPw(userPw);
        }
        memberRepository.save(MemberEntity.toUpdateMemberEntity(memberDto));

    }

    @Override
    public void memberDelete(Long id) {

    }

    @Override
    public void memberDeleteByUserEmailAndUserPw(MemberDto memberDto) {

    }

    @Override
    public Page<MemberDto> pagingSearchListAll(Pageable pageable, String subject, String search) {
        // 1. null & 공백 처리
        if (subject == null || subject.isBlank() || search == null || search.isBlank()) {
            return memberRepository.findAll(pageable)
                    .map(MemberDto::toMemberDto);
        }
        // 2. 검색 조건 처리
        Page<MemberEntity> memberEntities;

        switch (subject) {
            case "userEmail":
                memberEntities = memberRepository.findByUserEmailContaining(pageable, search);
                break;
            case "userName":
                memberEntities = memberRepository.findByUserNameContaining(pageable, search);
                break;
            default:
                memberEntities = memberRepository.findAll(pageable);
        }

        return memberEntities.map(MemberDto::toMemberDto);
//        return null;
    }
}
