package org.spring.security0512.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.spring.security0512.common.BasicTime;
import org.spring.security0512.dto.BoardDto;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "sec_board_tb2")
public class BoardEntity extends BasicTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;    // id, auto_increment

    @Column(nullable = false)
    private String title; //제목

    @Column(nullable = false)
    private String content; //내용

    @Column(nullable = false)
    private String boardPw;//글비빌번호

    @Column(nullable = false)
    private String nickName; //닉네임

    private int hit;    // 조회수 기본이 0

    // FileEntity 연관관계 추가
    // 파일 유무 체크
    @Column(nullable = false)
    private int attachFile; // 게시글 작성시 파일이 존재 1  ,없으면 0


    //연관관계
    //BoardEntity  N:1 MemberEntity
    // member_id -> 칼럼명이 자동으로 생김
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @JsonIgnore
    private MemberEntity memberEntity;// member_id
    //1:N
    @JsonIgnore
    @OneToMany(mappedBy = "boardEntity",
            fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE)
    private List<FileEntity> fileEntities;

    //dto -> entity

    //파일(이미지)이 없을 경우
    // 게시글작성
    public static BoardEntity toInsertBoardEntity(BoardDto boardDto) {
        BoardEntity boardEntity=new BoardEntity();
        boardEntity.setTitle(boardDto.getTitle());
        boardEntity.setContent(boardDto.getContent());
        boardEntity.setNickName(boardDto.getNickName());
        boardEntity.setBoardPw(boardDto.getBoardPw());
        boardEntity.setAttachFile(0);// 파일이없을 경우
        boardEntity.setHit(0);  //조회수 0
        boardEntity.setMemberEntity(boardDto.getMemberEntity());
        return boardEntity;
    }
    // 게시글수정
    public static BoardEntity toUpdateBoardEntity(BoardDto boardDto) {
        BoardEntity boardEntity=new BoardEntity();
        boardEntity.setId(boardDto.getId());
        boardEntity.setTitle(boardDto.getTitle());
        boardEntity.setContent(boardDto.getContent());
        boardEntity.setNickName(boardDto.getNickName());
        boardEntity.setBoardPw(boardDto.getBoardPw());
        boardEntity.setAttachFile(1);// 수정
//        boardEntity.setAttachFile(boardDto.getAttachFile());
        boardEntity.setHit(boardDto.getHit());
        boardEntity.setMemberEntity(boardDto.getMemberEntity());
        return boardEntity;
    }
    //파일(이미지)이 있을 경우
    // 게시글등록
    public static BoardEntity toInsertFileBoardEntity(BoardDto boardDto) {
        BoardEntity boardEntity=new BoardEntity();
        boardEntity.setTitle(boardDto.getTitle());
        boardEntity.setContent(boardDto.getContent());
        boardEntity.setNickName(boardDto.getNickName());
        boardEntity.setBoardPw(boardDto.getBoardPw());
        boardEntity.setAttachFile(1);
        boardEntity.setMemberEntity(boardDto.getMemberEntity());
        return boardEntity;
    }
    // 게시글수정
    public static BoardEntity toUpdateFileBoardEntity(BoardDto boardDto) {
        BoardEntity boardEntity=new BoardEntity();
        boardEntity.setId(boardDto.getId());
        boardEntity.setTitle(boardDto.getTitle());
        boardEntity.setContent(boardDto.getContent());
        boardEntity.setNickName(boardDto.getNickName());
        boardEntity.setBoardPw(boardDto.getBoardPw());
        boardEntity.setAttachFile(1);
        boardEntity.setMemberEntity(boardDto.getMemberEntity());
        return boardEntity;
    }
}
