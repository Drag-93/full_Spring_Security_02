package org.spring.security0512.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.spring.security0512.dto.MemberDto;
import org.spring.security0512.repository.MemberRepository;
import org.spring.security0512.service.MemberService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Log4j2
@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberRepository memberRepository;

    private final MemberService memberService;

    @GetMapping({"/memberList"})
    public String index( @PageableDefault(page = 0,size = 5, sort = "id",
                                     direction = Sort.Direction.DESC) Pageable pageable,
                         @RequestParam(name = "subject", required = false) String subject,
                         @RequestParam(name = "search", required = false) String search,
                         Model model) {
//    Page<BoardDto> boardList =boardService.pagingListAll(pageable);
        Page<MemberDto> memberList =memberService.pagingSearchListAll(pageable,subject,search);


        log.info("======memberList"+memberList+"====================");

//        long count=boardList.getTotalElements(); // 44
        int newPage = memberList.getNumber();// 기본이 0, 현재페이지
        int totalPages = memberList.getTotalPages(); //전체페이지  44 / 5  9
        int blockNum = 3;     // 블록 수

        int startPage = (newPage / blockNum) * blockNum + 1;        //시작페이지
        //블록의 끝
        int endPage = Math.min(startPage + blockNum - 1, totalPages);  // 끝페이지

        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("memberList", memberList);
        model.addAttribute("newPage", newPage);

        return "/member/memberList";
    }




    @GetMapping({"","/login"})
    public String login(@RequestParam(value = "error",required = false) String error, Model model, Authentication authentication){
        if(error!=null){
            model.addAttribute("error",true);
            model.addAttribute("exception","이메일 또는 비밀번호가 틀렸습니다");
        }
        if(authentication==null){

        return "member/login";
        }
        return "redirect:/member/memberList";

    }

    @GetMapping("/join")
    public String join(MemberDto memberDto){
        return "member/join";
    }
    @PostMapping("/join")
    public String joinPost(@Valid MemberDto memberDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "member/join";
        }
        //회원가입 실행
        memberService.memberInsert(memberDto);
        return "member/login";
    }

    @GetMapping("/detail/{id}")
//    public  String detail(@AuthenticationPrincipal MyUserDetails myUserDetails) // -> 커스텀으로 따로 저장해둠
    public String detail(@PathVariable("id") Long id, Model model){
        MemberDto memberDto=memberService.memberDetail(id);
        model.addAttribute("member",memberDto);
        return "member/detail";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable("id")Long id,Model model){
        MemberDto memberDto=memberService.memberDetail(id);
        model.addAttribute("member",memberDto);
        return "member/update";
    }

    @PostMapping("/update")
    public String updateOk(@Valid MemberDto memberDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "redirect:/member/detail/"+memberDto.getId();
        }
        //회원가입 실행
        memberService.memberUpdate(memberDto);
        return "redirect:/member/detail/"+memberDto.getId();
    }
}
