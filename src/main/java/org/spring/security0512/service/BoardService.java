package org.spring.security0512.service;

import org.spring.security0512.dto.BoardDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface BoardService {
    //게시글등록
    void boardInsert(BoardDto boardDto);

    void boardFileInsert(BoardDto boardDto) throws IOException;

    //게시글목록조회
    List<BoardDto> boardList();

    //게시글상세조회
    BoardDto boardDetail(Long id);

    //게시글삭제
    void boardDelete(Long id);

    //게시글 수정
//    void boardUpdate(BoardDto boardDto);

    //조회수 증가
    void updateHit(Long id);

    Page<BoardDto> pagingListAll(Pageable pageable);

    Page<BoardDto> pagingSearchListAll(Pageable pageable, String subject, String search);

    // 게시글 수정(파일)
    void boardUpdate(BoardDto boardDto)  throws IOException ;
}
