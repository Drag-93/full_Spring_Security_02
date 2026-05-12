package org.spring.security0512.repository;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.spring.security0512.entity.MemberEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    Optional<MemberEntity> findByUserEmail(@NotBlank(message = "이메일을 입력해주세요") @Size(min = 3) String userEmail);

    Page<MemberEntity> findByUserEmailContaining(Pageable pageable, String search);

    Page<MemberEntity> findByUserNameContaining(Pageable pageable, String search);

    boolean existsByUserEmail(String newUserEmail);
}
