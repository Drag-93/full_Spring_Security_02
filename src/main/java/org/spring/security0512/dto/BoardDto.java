package org.spring.security0512.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.spring.security0512.entity.BoardEntity;
import org.spring.security0512.entity.FileEntity;
import org.spring.security0512.entity.MemberEntity;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardDto {

    private Long id;    // id, auto_increment

    @NotBlank(message = "title 입력해주세요")
    private String title; //제목

    @NotBlank(message = "content 입력해주세요")
    private String content; //내용

    @NotBlank(message = "boardPw 입력해주세요")
    private String boardPw;//글비빌번호

    @NotBlank(message = "nickName 입력해주세요")
    private String nickName; //닉네임

    @NonNull
    private Long memberId; // 로그인 한 아이디

    private int hit;    // 조회수 기본이 0

    private MemberEntity memberEntity;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    // 파일(이미지) 추가
    //파일이 있을 경우  1, 없을 경우 0
    private int attachFile;
    //  input type="file"
    private MultipartFile boardFile; // 실제 파일(이미지)

    private String newFileName;// 새이름 ->DB,로컬저장소 저장이름

    private String oleFileName;// 원본이름

    public static BoardDto toBoardDto(BoardEntity boardEntity) {

        BoardDto boardDto = new BoardDto();

        boardDto.setId(boardEntity.getId());
        boardDto.setTitle(boardEntity.getTitle());
        boardDto.setContent(boardEntity.getContent());
        boardDto.setBoardPw(boardEntity.getBoardPw());
        boardDto.setHit(boardEntity.getHit());
        boardDto.setNickName(boardEntity.getNickName());

        if (boardEntity.getMemberEntity() != null) {
            boardDto.setMemberEntity(boardEntity.getMemberEntity());
            boardDto.setMemberId(boardEntity.getMemberEntity().getId());
        }
        boardDto.setCreateTime(boardEntity.getCreateTime());
        boardDto.setUpdateTime(boardEntity.getUpdateTime());
        // 파일 여부
        boardDto.setAttachFile(boardEntity.getAttachFile());
        // 파일 리스트 안전 처리
        if (boardEntity.getFileEntities() != null && !boardEntity.getFileEntities().isEmpty()) {

            FileEntity fileEntity = boardEntity.getFileEntities().get(0);

            boardDto.setNewFileName(fileEntity.getNewFileName());
            boardDto.setOleFileName(fileEntity.getOleFileName());
        }

        return boardDto;
    }
}
