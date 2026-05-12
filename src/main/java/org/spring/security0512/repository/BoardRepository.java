package org.spring.security0512.repository;

import org.spring.security0512.entity.BoardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<BoardEntity,Long> {

    @Modifying  // JPQL -> Entity
    @Query("update BoardEntity b set b.hit = b.hit + 1 where b.id = :id")
    void updateHit(@Param("id") Long id);

//    Page<BoardEntity> findByTitleContaining(Pageable pageable, String search);
//
//    Page<BoardEntity> findByContentContaining(Pageable pageable, String search);
//
//    Page<BoardEntity> findByNickNameContaining(Pageable pageable, String search);

    @EntityGraph(attributePaths = {"memberEntity"}) // 회원정보 쿼리를 한번만 호출
    Page<BoardEntity> findByTitleContaining(Pageable pageable, String search);

    @EntityGraph(attributePaths = {"memberEntity"})
    Page<BoardEntity> findByContentContaining(Pageable pageable, String search);

    @EntityGraph(attributePaths = {"memberEntity"})
    Page<BoardEntity> findByNickNameContaining(Pageable pageable, String search);

    @EntityGraph(attributePaths = {"memberEntity"})
    Page<BoardEntity> findAll(Pageable pageable);
}

