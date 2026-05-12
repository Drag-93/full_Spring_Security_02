package org.spring.security0512.dto;

import lombok.*;
import org.spring.security0512.entity.BoardEntity;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class FileDto {

    private Long id;

    private String newFileName;// 새이름 ->DB,로컬저장소 저장이름

    private String oleFileName;// 원본이름

    private Long boardId; // board_id

    private BoardEntity boardEntity;//board.getMemberEntity().id

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}
