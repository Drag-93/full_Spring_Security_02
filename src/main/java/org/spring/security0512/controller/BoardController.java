package org.spring.security0512.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.spring.security0512.dto.BoardDto;
import org.spring.security0512.service.BoardService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Log4j2
@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    //파일
    @GetMapping("/write")
    public String write2(BoardDto boardDto){
        return "/pages/board/boardWrite";
    }

    @PostMapping("/write")
    public String writeOk2(@Valid BoardDto boardDto,
                           BindingResult bindingResult) throws IOException {
        if (bindingResult.hasErrors()) {
            return "/board/boardWrite";
        }
        boardService.boardFileInsert(boardDto);

        return "redirect:/board/boardList";
    }


    @GetMapping({"","/boardList"})  // 기본요청: 0페이지 5개 id별로 내림차순
    public String boardList4( @PageableDefault(page = 0,size = 5, sort = "id",
                                      direction = Sort.Direction.DESC) Pageable pageable,
                              @RequestParam(name = "subject", required = false) String subject,
                              @RequestParam(name = "search", required = false) String search,
                              Model model) {

        Page<BoardDto> boardList =boardService.pagingSearchListAll(pageable,subject,search);

        long count=boardList.getTotalElements(); // 44
        int newPage = boardList.getNumber();// 기본이 0, 현재페이지
        int totalPages = boardList.getTotalPages(); //전체페이지  44 / 5  9
        int blockNum = 3;     // 블록 수

        int startPage = (newPage / blockNum) * blockNum + 1;        //시작페이지
        //블록의 끝
        int endPage = Math.min(startPage + blockNum - 1, totalPages);  // 끝페이지

        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("boardList", boardList);
        model.addAttribute("newPage", newPage);

        return "/board/boardList";
    }

    @GetMapping("/detail/{id}")
    public String detail2(@PathVariable("id")Long id,
                          Model model){
        // hit
        boardService.updateHit(id);

        BoardDto boardDto=boardService.boardDetail(id);
        model.addAttribute("board",boardDto);
        return "/board/boardDetail";
    }


    //파일 -> 게시글 수정
    @PostMapping("/update") //form
    public String update(@ModelAttribute BoardDto boardDto, Model model)
            throws IOException{

        boardService.boardUpdate(boardDto);;

        return "redirect:/board/boardList";//수정후 게시글 목록
    }

    @GetMapping("/delete/{id}")
    public String delete( @PathVariable("id")Long id){
        boardService.boardDelete(id);
        return "redirect:/board/boardList";
    }

}