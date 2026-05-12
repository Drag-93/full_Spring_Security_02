package org.spring.security0512.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.spring.security0512.dto.BoardDto;
import org.spring.security0512.dto.FileDto;
import org.spring.security0512.entity.BoardEntity;
import org.spring.security0512.entity.FileEntity;
import org.spring.security0512.entity.MemberEntity;
import org.spring.security0512.repository.BoardRepository;
import org.spring.security0512.repository.FileRepository;
import org.spring.security0512.repository.MemberRepository;
import org.spring.security0512.service.BoardService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BoardServiceImpl implements BoardService {

    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final FileRepository fileRepository;

    @Transactional
    @Override
    public void boardInsert(BoardDto boardDto) {
        //1, 작성자 확인
//        Optional<MemberEntity> optionalMemberEntity
//                = memberRepository.findById(boardDto.getMemberId());
//        if (!optionalMemberEntity.isPresent()) {
//            throw new IllegalArgumentException("Fail-> 회원정보가 없습니다!");
//        }
        //작성자 확인 없으면 예외 발생
        memberRepository.findById(boardDto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Fail-> 회원정보가 없습니다!"));

        //파일이 없을 경우
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setId(boardDto.getMemberId()); //form memberId-> Dto -> Dto MemberEntity

        boardDto.setMemberEntity(memberEntity);// Dto MemberEntity
        // 게시글 작성
        boardRepository.save(BoardEntity.toInsertBoardEntity(boardDto));

        //파일이 있을 경우

    }


    @Transactional
    @Override
    public void boardFileInsert(BoardDto boardDto) throws IOException {
        // 1. 작성자
        MemberEntity memberEntity = MemberEntity.builder()
                .id(boardDto.getMemberId())
                .build();
        boardDto.setMemberEntity(memberEntity);
        // 2. 파일 없는 경우
        if (boardDto.getBoardFile().isEmpty()) {
            BoardEntity boardEntity = BoardEntity.toInsertBoardEntity(boardDto);
            boardRepository.save(boardEntity);
            return;
        }
        // 3. 파일 있는 경우
        MultipartFile boardFile = boardDto.getBoardFile();

        String oldFileName = boardFile.getOriginalFilename();
        String newFileName = UUID.randomUUID() + "_" + oldFileName;
        String fileSavePath = "E:/full/upload/test0508/" + newFileName;

        // 파일 저장
        boardFile.transferTo(new File(fileSavePath));

        // 게시글 저장
        BoardEntity boardEntity = BoardEntity.toInsertFileBoardEntity(boardDto);
        BoardEntity savedBoard = boardRepository.save(boardEntity);

        // 파일 DB 저장
        FileDto fileDto = FileDto.builder()
                .oleFileName(oldFileName)
                .newFileName(newFileName)
                .boardEntity(savedBoard)
                .build();
        fileRepository.save(FileEntity.toInsertFile(fileDto));
    }

    //게시글수정(file)
    @Override
    public void boardUpdate(BoardDto boardDto) throws IOException {
        // 1. 기존 게시글 조회
        BoardEntity originBoard = boardRepository.findById(boardDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("게시글이 없습니다."));
        // 2. 회원 조회 ( 핵심: 반드시 DB에서 가져와야 함)
        MemberEntity memberEntity = memberRepository.findById(boardDto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("회원이 없습니다."));
        // 3. 파일 없는 경우 (기존 파일 유지)
        if (boardDto.getBoardFile() == null || boardDto.getBoardFile().isEmpty()) {
            BoardEntity updateEntity = BoardEntity.builder()
                    .id(originBoard.getId())
                    .title(boardDto.getTitle())
                    .content(boardDto.getContent())
                    .boardPw(boardDto.getBoardPw())
                    .nickName(boardDto.getNickName())
                    .hit(originBoard.getHit())
                    .attachFile(originBoard.getAttachFile())
                    .memberEntity(memberEntity)
                    .build();
            boardRepository.save(updateEntity);
            return;
        }
        // 4. 파일 있는 경우 → 기존 파일 삭제
        fileRepository.findByBoardEntityId(boardDto.getId())
                .ifPresent(file -> {
                    File oldFile = new File("E:/full/upload/" + file.getNewFileName());
                    if (oldFile.exists()) oldFile.delete();
                    fileRepository.delete(file);
                });
        // 5. 새 파일 저장
        MultipartFile uploadFile = boardDto.getBoardFile();
        String oldFileName = uploadFile.getOriginalFilename();
        String newFileName = UUID.randomUUID() + "_" + oldFileName;
        uploadFile.transferTo(new File("E:/full/upload/test0508/" + newFileName));
        // 6. 게시글 업데이트
        BoardEntity updatedBoard = BoardEntity.builder()
                .id(originBoard.getId())
                .title(boardDto.getTitle())
                .content(boardDto.getContent())
                .boardPw(boardDto.getBoardPw())
                .nickName(boardDto.getNickName())
                .hit(originBoard.getHit())
                .attachFile(boardDto.getBoardFile().isEmpty() ? originBoard.getAttachFile() : 1)
                .memberEntity(memberEntity)
                .build();
        BoardEntity savedBoard = boardRepository.save(updatedBoard);

        // 7. 파일 엔티티 저장
        FileEntity fileEntity = FileEntity.builder()
                .oleFileName(oldFileName)
                .newFileName(newFileName)
                .boardEntity(savedBoard)
                .build();
        fileRepository.save(fileEntity);
    }
    @Override
    public List<BoardDto> boardList() {
        return boardRepository.findAll().stream()
                .map(BoardDto::toBoardDto)
                .collect(Collectors.toList());
    }

    @Override
    public BoardDto boardDetail(Long id) {
        BoardEntity boardEntity = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Fail-> board id"));
        return BoardDto.toBoardDto(boardEntity);

//        return boardRepository.findById(id).orElseThrow(()->{
//            throw new IllegalArgumentException("Fail-> board id");
//        });
    }

    @Transactional
    @Override
    public void boardDelete(Long id) {
        boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Fail-> board id"));

        // 파일 존재하는지  체크                    select * from 파일  where board_id= 게시글아이디
        // Optional<FileEntity> optionalFileEntity = fileRepository.findByBoardEntityId(boardEntity.getId());
        // 파일 존재하는지  체크                     select * from 파일 게시글Entity
        Optional<FileEntity> optionalFileEntity = fileRepository.findByBoardEntityId(id);
        // 파일이 있으면 파일 기존 파일 삭제
        if(optionalFileEntity.isPresent()) {
            String fileNewName = optionalFileEntity.get().getNewFileName(); //DB저장 파일이름
            String filePath = "E:/full/upload/test0508/" + fileNewName;
            File deleteFile = new File(filePath);
            if(deleteFile.exists()) {
                deleteFile.delete(); // 파일 삭제(로컬)
                System.out.println("파일을 삭제하였습니다.");
            } else {
                System.out.println("파일이 존재하지 않습니다.");
            }
            fileRepository.delete(optionalFileEntity.get()); // 파일 테이블 레코드 삭제
        }

        boardRepository.deleteById(id);
    }

//    @Override
//    public void boardUpdate(BoardDto boardDto) {
//        boardRepository.findById(boardDto.getId())
//                .orElseThrow(() -> new IllegalArgumentException("Fail-> board id"));
//
//        //memberId -> Dto -> Entity변환
//        MemberEntity memberEntity = new MemberEntity();
//        memberEntity.setId(boardDto.getMemberId());
//        boardDto.setMemberEntity(memberEntity);
//
//        boardRepository.save(BoardEntity.toUpdateBoardEntity(boardDto));
//    }

    @Transactional
    @Override
    public void updateHit(Long id) {
        //조회수
        boardRepository.updateHit(id);
    }

    @Override
    public Page<BoardDto> pagingListAll(Pageable pageable) {
        Page<BoardEntity> boardEntities = boardRepository.findAll(pageable);
        return boardEntities.map(BoardDto::toBoardDto);
    }

    @Override
    public Page<BoardDto> pagingSearchListAll(Pageable pageable, String subject, String search) {

        // 1. null & 공백 처리
        if (subject == null || subject.isBlank() || search == null || search.isBlank()) {
            return boardRepository.findAll(pageable)
                    .map(BoardDto::toBoardDto);
        }
        // 2. 검색 조건 처리
        Page<BoardEntity> boardEntities;

        switch (subject) {
            case "title":
                boardEntities = boardRepository.findByTitleContaining(pageable, search);
                break;
            case "content":
                boardEntities = boardRepository.findByContentContaining(pageable, search);
                break;
            case "nickName":
                boardEntities = boardRepository.findByNickNameContaining(pageable, search);
                break;
            default:
                boardEntities = boardRepository.findAll(pageable);
        }

        // 3. DTO 변환
        return boardEntities.map(BoardDto::toBoardDto);
    }

}
