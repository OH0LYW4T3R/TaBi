package com.example.tabi.appuser.service.email;

import com.example.tabi.appuser.service.AppUserService;
import com.example.tabi.appuser.service.AppUserSignUpService;
import com.example.tabi.appuser.service.AppUserSignUpServiceJpaImpl;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailAuthenticationServiceImpl implements EmailAuthenticationService{
    private final JavaMailSender javaMailSender;
    @Value("${GMAIL_EMAIL}")
    private String GMAIL_EMAIL;

    @Override
    public String issuanceEmailCode(String email) {
        String code = generateAuthCode();
        final String htmlContent = getHtmlContent(code);

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject("[TaBi] 이메일 인증 코드");
            helper.setFrom("TaBi <" + GMAIL_EMAIL + ">");
            helper.setText(htmlContent, true);

            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException("이메일 전송 실패");
        }

        return code;
    }

    private static String getHtmlContent(String code) {
        int expiration = AppUserSignUpServiceJpaImpl.EXPIRATION_TIME_MINUTE;

        String htmlContent = "<div style='font-family: Arial, sans-serif;'>" +
                "<h2 style='color: #222222;'>[TaBi] 이메일 인증 코드</h2>" +
                "<p>아래 코드를 입력하여 이메일 인증을 완료해 주세요:)</p>" +
                "<div style='font-size: 24px; font-weight: bold; color: #61402D; padding: 12px 0;'>" +
                code +
                "</div>" +
                "<p style='font-size: 12px; color: gray;'>이 코드는 " + expiration + "분 동안 유효합니다.</p>" +
                "</div>";

        return htmlContent;
    }

    public String generateAuthCode() {
        int code = (int) (Math.random() * 900000) + 100000;
        return String.valueOf(code);
    }
}
