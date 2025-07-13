package com.example.tabi.appuser.repository;

import com.example.tabi.appuser.entity.EmailAuthCode;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface EmailAuthCodeRepository extends JpaRepository<EmailAuthCode, Long> {
    Optional<EmailAuthCode> findByEmail(String email);
    @Modifying
    @Transactional
    @Query("DELETE FROM EmailAuthCode e WHERE e.expirationTime < :now")
    void deleteAllExpiredBefore(@Param("now") LocalDateTime now);
}
