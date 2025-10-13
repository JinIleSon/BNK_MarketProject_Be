package kr.co.bnk_marketproject_be.service;

import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import kr.co.bnk_marketproject_be.dto.SessionDataDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String sender;

    private final SessionDataDTO sessionData;

    /**
     * 이메일로 인증코드를 전송하고, 세션에 코드와 타임스탬프를 저장한다.
     * 실패 시 RuntimeException을 던진다(컨트롤러가 500을 반환).
     */
    public void sendCode(String receiver) {
        log.info(">>> EmailService.sendCode called, receiver={}", receiver);

        MimeMessage message = mailSender.createMimeMessage();

        int code = ThreadLocalRandom.current().nextInt(100000, 1_000_000);
        String codeStr = String.valueOf(code);

        String title = "NICHIYA 인증코드 입니다.";
        String content = "<h1>인증코드는 " + codeStr + "입니다.</h1>";

        try {
            // From (발신자), To, Subject, Content 설정
            message.setFrom(new InternetAddress(sender, "NICHIYA", StandardCharsets.UTF_8.name()));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiver));
            message.setSubject(title);
            message.setContent(content, "text/html;charset=UTF-8");

            // 메일 전송
            mailSender.send(message);
            log.info(">>> EmailService: mail sent to {}", receiver);

            // 세션에 인증코드와 타임스탬프 저장, 인증 상태 리셋
            sessionData.setCode(codeStr);
            sessionData.setCodeTimestamp(System.currentTimeMillis());
            sessionData.setVerified(false); // 새 코드를 발급하면 기존 인증은 무효
            log.debug(">>> EmailService: sessionData updated (code set, verified=false)");
        } catch (Exception e) {
            log.error(">>> EmailService: failed to send email to {} - {}", receiver, e.getMessage(), e);
            // 예외를 다시 던져서 컨트롤러가 실패를 인지하도록 함
            throw new RuntimeException("메일 전송 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }

    /**
     * 세션에 저장된 코드와 비교하여 일치하면 세션의 verified 플래그를 true로 설정한다.
     * 코드가 null이거나 일치하지 않으면 false 반환.
     */
    public boolean verifyCode(String code) {
        log.info(">>> EmailService.verifyCode called, inputCode={}", code);

        String sessCode = sessionData.getCode();
        if (sessCode == null) {
            log.warn(">>> EmailService.verifyCode: no code in session (maybe expired or not issued)");
            return false;
        }

        boolean matched = sessCode.equals(code);
        if (matched) {
            sessionData.setVerified(true);
            log.info(">>> EmailService.verifyCode: code matched -> session verified set true");
        } else {
            log.info(">>> EmailService.verifyCode: code did not match (session={}, input={})", sessCode, code);
        }
        return matched;
    }
}
