package org.spring.security0512;

import org.junit.jupiter.api.Test;
import org.spring.security0512.dto.BoardDto;
import org.spring.security0512.entity.BoardEntity;
import org.spring.security0512.entity.MemberEntity;
import org.spring.security0512.repository.BoardRepository;
import org.spring.security0512.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BoardTest {

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    MemberRepository memberRepository;

    @Test
    void insertBoard() {
        for (int i = 0; i < 30; i++) {

            MemberEntity memberEntity = new MemberEntity();
            memberEntity.setId(102L);

            BoardDto boardDto = new BoardDto();
            boardDto.setBoardPw("12" + i);
            boardDto.setContent("내용" + 1);
            boardDto.setTitle("제목" + i);
            boardDto.setNickName("tester");
            boardDto.setMemberEntity(memberEntity);

            BoardEntity boardEntity2 = BoardEntity.toInsertBoardEntity(boardDto);
            boardRepository.save(boardEntity2);
            //Builder패턴
//            BoardEntity boardEntity= BoardEntity.toInsertBoardEntity(BoardDto.builder()
//                    .title("제목" + i)
//                    .content("내용" + i)
//                    .boardPw("12" + i)
//                    .nickName("m1" + i)
//                    .memberEntity(MemberEntity.builder().id(1L).build())
////                    .memberEntity(memberEntity)
//                    .build());

            //        boardRepository.save(boardEntity);
        }
        System.out.println("===================== list");
    }

}