package org.spring.security0512.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.spring.security0512.common.BasicTime;
import org.spring.security0512.dto.FileDto;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "sec_file_tb2")
public class FileEntity extends BasicTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long id;

    @Column(nullable = false)
    private String newFileName;// 새이름 ->DB,로컬저장소 저장이름

    @Column(nullable = false)
    private String oleFileName;// 원본이름

    //N:1
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private BoardEntity boardEntity;

    public static FileEntity toInsertFile(FileDto fileDto) {
        FileEntity fileEntity=new FileEntity();
        fileEntity.setNewFileName(fileDto.getNewFileName());
        fileEntity.setOleFileName(fileDto.getOleFileName());
        fileEntity.setBoardEntity(fileDto.getBoardEntity());
        return fileEntity;


    }
}