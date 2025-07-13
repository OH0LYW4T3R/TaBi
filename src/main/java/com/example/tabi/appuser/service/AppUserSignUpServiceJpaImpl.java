package com.example.tabi.appuser.service;

import com.example.tabi.appuser.entity.AppUser;
import com.example.tabi.appuser.entity.EmailAuthCode;
import com.example.tabi.appuser.repository.AppUserRepository;
import com.example.tabi.appuser.repository.EmailAuthCodeRepository;
import com.example.tabi.appuser.service.email.EmailAuthenticationService;
import com.example.tabi.appuser.vo.AppUserDto;
import com.example.tabi.appuser.vo.AppUserRequest;
import com.example.tabi.appuser.vo.EmailAuthRequest;
import com.example.tabi.appuser.vo.EmailRequest;
import com.example.tabi.member.entity.Member;
import com.example.tabi.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppUserSignUpServiceJpaImpl implements AppUserSignUpService {
    private final AppUserRepository appUserRepository;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailAuthenticationService emailAuthenticationService;
    private final EmailAuthCodeRepository emailAuthCodeRepository;
    private static final int SCHEDULE_TIME = 12 * 60 * 60 * 1000;
    public static final int EXPIRATION_TIME_MINUTE = 5;

    @Transactional
    @Override
    public AppUserDto createAppUser(AppUserRequest appUserRequest) {
        AppUser appUser = new AppUser();
        appUser.setUsername(appUserRequest.getUsername());
        appUser.setBirth(appUserRequest.getBirth());
        appUser.setGender(appUserRequest.isGender());
        appUser.setMobileCarrier(appUserRequest.getMobileCarrier());
        appUser.setPhoneNumber(appUserRequest.getPhoneNumber());
        log.info("Agreement : {}", appUserRequest.isAgreement());
        System.out.println(appUserRequest.isAgreement());
        appUser.setAgreement(appUserRequest.isAgreement());
        appUser.setLocked(false);

        Member member = new Member();
        member.setEmail(appUserRequest.getEmail());
        member.setPassword(passwordEncoder.encode(appUserRequest.getPassword()));

        appUser.setMember(member);
        member.setAppUser(appUser);

        appUserRepository.save(appUser);
        memberRepository.save(member);

        return AppUserServiceJpaImpl.appUserToAppUserDto(appUser);
    }

    @Transactional
    @Override
    public Boolean generateEmailAuthenticationCode(EmailRequest emailRequest) {
        String email = emailRequest.getEmail();

        if (emailDuplicateCheck(email))
            return true; // 이미 존재하여 인증코드를 보낼 수 없음.

        String code = emailAuthenticationService.issuanceEmailCode(email);

        if (code == null)
            return true; // 코드를 생성할 수 업는 예외적인 경우

        Optional<EmailAuthCode> existing = emailAuthCodeRepository.findByEmail(email);
        EmailAuthCode emailAuthCode = null;

        if (existing.isPresent()) {
            emailAuthCode = existing.get();
            emailAuthCode.setAuthCode(code);
            emailAuthCode.setExpirationTime(LocalDateTime.now().plusMinutes(EXPIRATION_TIME_MINUTE));
        } else {
            emailAuthCode = new EmailAuthCode();
            emailAuthCode.setEmail(email);
            emailAuthCode.setAuthCode(code);
            emailAuthCode.setExpirationTime(LocalDateTime.now().plusMinutes(EXPIRATION_TIME_MINUTE));
        }

        emailAuthCodeRepository.save(emailAuthCode);

        return false;
    }

    @Transactional
    @Override
    public Boolean verifyEmailCode(EmailAuthRequest emailAuthRequest) {
        String email = emailAuthRequest.getEmail();
        String code = emailAuthRequest.getCode();

        Optional<EmailAuthCode> existing = emailAuthCodeRepository.findByEmail(email);

        if (existing.isEmpty())
            return false; // 발급한 코드가 없는 경우

        EmailAuthCode emailAuthCode = existing.get();

        if (emailAuthCode.getExpirationTime().isBefore(LocalDateTime.now())) {
            emailAuthCodeRepository.delete(emailAuthCode);
            return false; // 인증 코드가 만료됨
        }

        if (!emailAuthCode.getAuthCode().equals(code))
            return false; // 이메일 코드와 다른 경우

        emailAuthCodeRepository.delete(emailAuthCode); // 인증 완료된 경우에는 db에서 삭제

        return true;
    }

    @Scheduled(fixedRate = SCHEDULE_TIME)
    @Override
    public void removeExpiredCode() {
        emailAuthCodeRepository.deleteAllExpiredBefore(LocalDateTime.now());
    }

    private Boolean emailDuplicateCheck(String email) {
        Member member = memberRepository.findByEmail(email);
        // 존재하면 True, 없으면 False
        return member != null;
    }
}
