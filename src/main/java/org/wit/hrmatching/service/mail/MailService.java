package org.wit.hrmatching.service.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    public void sendVerificationMail(String toEmail, String token) {
        String subject = "HR 매칭 서비스 이메일 인증";
        String verificationLink = "http://localhost:8080/users/verify?token=" + token;

        String content = """
                <h2>이메일 인증을 완료해주세요.</h2>
                <p>아래 링크를 클릭하면 인증이 완료됩니다:</p>
                <a href="%s">이메일 인증하기</a>
                """.formatted(verificationLink);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(content, true);

            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("이메일 전송 실패", e);
        }
    }
}
