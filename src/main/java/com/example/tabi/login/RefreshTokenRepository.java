package com.example.tabi.login;

import com.example.tabi.appuser.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByAppUser(AppUser appUser);
    RefreshToken findByRefreshToken(String refreshToken);
}
