package org.wit.hrmatching.service.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    @Value("${custom.api.base-url}")
    private String url;

    @Async
    public void sendVerificationMail(String toEmail, String token) {
        String subject = "HR 매칭 서비스 이메일 인증";
        String verificationLink = "http://" + url + ":8080/users/verify?token=" + token;

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

            System.out.println("메일 전송 완료: " + toEmail);

        } catch (MessagingException e) {
            throw new RuntimeException("이메일 전송 실패", e);
        }
    }

    @Async
    public void sendInquiryAnswerNotification(String toEmail, String userName, Long inquiryId) {
        String subject = "HR 매칭 서비스 - 문의 답변이 등록되었습니다";
        String link = "http://localhost:8080/inquiries/" + inquiryId;

        String content = """
            <h2>안녕하세요, %s 님</h2>
            <p>문의하신 내용에 대한 답변이 등록되었습니다.</p>
            <p>아래 링크를 클릭하시면 답변 내용을 확인하실 수 있습니다.</p>
            <a href="%s">답변 확인하러 가기</a>
            <br><br>
            <p>HR 매칭 서비스를 이용해주셔서 감사합니다.</p>
            """.formatted(userName, link);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(content, true);

            mailSender.send(message);

            System.out.println("문의 답변 메일 전송 완료: " + toEmail);

        } catch (MessagingException e) {
            throw new RuntimeException("문의 답변 이메일 전송 실패", e);
        }
    }

}
