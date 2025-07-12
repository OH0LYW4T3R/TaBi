package com.example.tabi.login;

import com.example.tabi.appuser.entity.AppUser;
import com.example.tabi.appuser.repository.AppUserRepository;
import com.example.tabi.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AppUserRepository appUserRepository;
    private final MemberRepository memberRepository;
    private static final Integer MAX_DAYS = 8;

    // Refresh Token 서버 저장
    // 로그인 -> 발급 -> 로그아웃 or 재로그인
    // 재로그인 -> 이메일로 유저 찾고 -> 토큰 있으면 이미 있으므로 아무일도 발생 x -> 생성된 리프레시 토큰은 무효화 됨. (이러면 동시접속이 안되는거 한쪽 리프레시는 만료)
    // 이미 있으면 그걸 반환 할것인가? or 이미 있으면 덮어 씌울것인가?
    @Transactional
    public void createRefreshToken(String email, String refreshToken) {
        AppUser appUser = appUserRepository.findByAppUserId(memberRepository.findByEmail(email).getAppUser().getAppUserId());
        Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findByAppUser(appUser);

        // Refresh DB에는 토큰 세션은 하나만 유지
        optionalRefreshToken.ifPresent(token -> deleteByRefreshToken(token.getRefreshToken()));

        RefreshToken newRefreshToken = new RefreshToken();
        newRefreshToken.setAppUser(appUser);
        newRefreshToken.setRefreshToken(refreshToken);
        newRefreshToken.setExpiryDate(Instant.now().plus(7, ChronoUnit.DAYS));

        refreshTokenRepository.save(newRefreshToken);
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

    public boolean updateRefreshToken(HttpServletResponse response, String refreshToken) {
        RefreshToken stringToRefreshToken = refreshTokenRepository.findByRefreshToken(refreshToken);
        if (stringToRefreshToken == null) return false; // 리프레시 소멸 -> 이미 로그아웃 된 사용자인 경우

        AppUser tokenUser = stringToRefreshToken.getAppUser();

        long daysLeft = ChronoUnit.DAYS.between(Instant.now(), stringToRefreshToken.getExpiryDate());
        if (daysLeft < MAX_DAYS) {
            String newRefreshToken = jwtUtil.generateRefreshToken(tokenUser.getMember().getEmail());

            stringToRefreshToken.setRefreshToken(newRefreshToken);
            stringToRefreshToken.setExpiryDate(Instant.now().plus(7, ChronoUnit.DAYS));
            refreshTokenRepository.save(stringToRefreshToken);

            ResponseCookie newCookie = ResponseCookie.from("refreshToken", newRefreshToken)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(7 * 24 * 60 * 60)
                .build();
            response.setHeader(HttpHeaders.SET_COOKIE, newCookie.toString());
        }

        return true;
    }
}
