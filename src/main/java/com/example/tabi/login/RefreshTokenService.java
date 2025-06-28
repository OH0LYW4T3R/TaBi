package com.example.tabi.login;

import com.example.tabi.appuser.entity.AppUser;
import com.example.tabi.appuser.repository.AppUserRepository;
import com.example.tabi.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final AppUserRepository appUserRepository;
    private final MemberRepository memberRepository;
    private static final Integer MAX_DAYS = 1;

    public void createOrUpdateToken(String email, String refreshToken) {
        AppUser appUser = appUserRepository.findByAppUserId(memberRepository.findByEmail(email).getAppUser().getAppUserId());
        Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findByAppUser(appUser);

        //Instant now = Instant.now();
        //Instant newExpiryDate = now.plus(7, ChronoUnit.DAYS);

        if (optionalRefreshToken.isEmpty()) {
            RefreshToken newRefreshToken = new RefreshToken();
            newRefreshToken.setAppUser(appUser);
            newRefreshToken.setRefreshToken(refreshToken);
            newRefreshToken.setExpiryDate(Instant.now().plus(7, ChronoUnit.DAYS));

            refreshTokenRepository.save(newRefreshToken);
        } else {
            //long daysLeft = ChronoUnit.DAYS.between(now, optionalRefreshToken.get().getExpiryDate());

            optionalRefreshToken.get().setRefreshToken(refreshToken);
            optionalRefreshToken.get().setExpiryDate(Instant.now().plus(7, ChronoUnit.DAYS));

            refreshTokenRepository.save(optionalRefreshToken.get());
        }
    }

    public AppUser validateRefreshToken(String refreshToken) {
        RefreshToken checkRefreshToken = refreshTokenRepository.findByRefreshToken(refreshToken);

        if (checkRefreshToken == null)
            return null;

        if (checkRefreshToken.getExpiryDate().isAfter(Instant.now())) {
            return refreshTokenRepository.findByRefreshToken(refreshToken).getAppUser();
        }

        return null;
    }

    public void deleteByRefreshToken(String refreshToken) {
        RefreshToken targetRefreshToken = refreshTokenRepository.findByRefreshToken(refreshToken);
        refreshTokenRepository.delete(targetRefreshToken);
    }
}
