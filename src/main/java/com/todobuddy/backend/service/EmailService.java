package com.todobuddy.backend.service;

import com.todobuddy.backend.exception.common.CommonErrorCode;
import com.todobuddy.backend.exception.common.EmailSendFailedException;
import com.todobuddy.backend.mail.EmailMessage;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    public String sendMail(EmailMessage emailMessage) {
        String verifyCode = createVerifyCode();

        MimeMessage mimeMessage = mailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false,
                "UTF-8");
            mimeMessageHelper.setTo(emailMessage.getTo());
            mimeMessageHelper.setSubject(emailMessage.getSubject());
            mimeMessageHelper.setText(createHtmlMessage(emailMessage.getMessage(), verifyCode),
                true);
            mailSender.send(mimeMessage);

            log.info("이메일 전송 성공");

            return verifyCode;
        } catch (MessagingException e) {
            log.info("이메일 전송 실패", e);
            throw new EmailSendFailedException(CommonErrorCode.EMAIL_SEND_FAILED);
        }
    }

    // 인증 코드 생성
    private String createVerifyCode() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    // 인증 코드를 포함한 HTML 메시지 생성
    private String createHtmlMessage(String message, String verifyCode) {
        return "<!DOCTYPE html>" +
            "<html lang=\"en\">" +
            "<head>" +
            "<meta charset=\"UTF-8\">" +
            "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
            "<title>Verification Code</title>" +
            "<style>" +
            "  body { font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f4f4f4; }"
            +
            "  .container { max-width: 600px; margin: 0 auto; padding: 20px; background-color: #ffffff; border-radius: 8px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }"
            +
            "  .header { background-color: #232f3e; padding: 20px; text-align: center; color: white; border-top-left-radius: 8px; border-top-right-radius: 8px; }"
            +
            "  .header img { width: 100px; }" +
            "  .content { padding: 20px; text-align: center; }" +
            "  .verify-code { font-size: 32px; font-weight: bold; color: #232f3e; margin: 20px 0; }"
            +
            "  .message { font-size: 16px; color: #555; }" +
            "  .footer { margin-top: 20px; text-align: center; color: #999; font-size: 12px; }" +
            "</style>" +
            "</head>" +
            "<body>" +
            "<div class=\"container\">" +
            "  <div class=\"header\">" +
            "  </div>" +
            "  <div class=\"content\">" +
            "    <p class=\"message\">" + message + "</p>" +
            "    <p class=\"verify-code\">" + verifyCode + "</p>" +
            "    <p class=\"message\">(이 코드는 전송 10분 후에 만료됩니다.)</p>" +
            "  </div>" +
            "  <div class=\"footer\">" +
            "    <p>&copy; TodoBuddy Services, lnc. All rights reserved.</p>" +
            "  </div>" +
            "</div>" +
            "</body>" +
            "</html>";
    }
}
