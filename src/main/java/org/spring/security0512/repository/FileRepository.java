package org.spring.security0512.repository;

import org.spring.security0512.entity.BoardEntity;
import org.spring.security0512.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileRepository extends JpaRepository<FileEntity,Long> {

    Optional<FileEntity> findByBoardEntityId(Long id);

    Optional<FileEntity> findFirstByBoardEntity_Id(Long boardId);

    Optional<FileEntity> findByBoardEntity(BoardEntity boardEntity);
}
