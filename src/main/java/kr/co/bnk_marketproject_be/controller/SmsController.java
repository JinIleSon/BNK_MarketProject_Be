package kr.co.bnk_marketproject_be.controller;

import kr.co.bnk_marketproject_be.service.SolapiSmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@Slf4j
@RequestMapping("/api/sms")
public class SmsController {

    private final SolapiSmsService smsService;

    private final ConcurrentHashMap<String, CodeEntry> codeStorage = new ConcurrentHashMap<>();
    private static final long CODE_TTL_SECONDS = 300; // 5분

    private record CodeEntry(String code, Instant expiresAt) {}

    private String normalizePhone(String p) {
        return p == null ? null : p.replaceAll("\\D", "");
    }

    public SmsController(SolapiSmsService smsService) {
        this.smsService = smsService;
    }

    @PostMapping(value = "/send", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> sendSms(@RequestBody Map<String, String> request) {
        String rawPhone = request.get("phoneNumber");
        String phone = normalizePhone(rawPhone);

        if (phone == null || !phone.matches("^01\\d{8,9}$")) {
            return ResponseEntity.badRequest().body(Map.of(
                    "ok", false, "message", "휴대폰 번호 형식이 올바르지 않습니다."
            ));
        }

        try {
            String code = smsService.sendVerificationCode(phone);

            // 코드 저장 (5분 만료)
            codeStorage.put(phone, new CodeEntry(code, Instant.now().plusSeconds(CODE_TTL_SECONDS)));

            // 개발 중 디버그 로그 (운영에는 인증코드 미노출!)
            log.debug("SMS 코드 발급: phone={}, code={}", phone, code);

            return ResponseEntity.ok(Map.of(
                    "ok", true,
                    "message", "인증번호 전송 완료"
                    // 보안상 code는 응답에 포함하지 않음
            ));
        } catch (Exception e) {
            log.error("SMS 전송 실패: phone={}, err={}", phone, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "ok", false, "message", "인증번호 전송에 실패했습니다."
            ));
        }
    }

    @PostMapping(value = "/verify", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> verifyCode(@RequestBody Map<String, String> request) {
        String rawPhone = request.get("phoneNumber");
        String inputCode = request.get("code");
        String phone = normalizePhone(rawPhone);

        if (phone == null || inputCode == null || inputCode.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "ok", false, "message", "요청 값이 올바르지 않습니다."
            ));
        }

        CodeEntry entry = codeStorage.get(phone);
        if (entry == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "ok", false, "message", "인증번호를 먼저 요청해 주세요."
            ));
        }

        if (Instant.now().isAfter(entry.expiresAt())) {
            codeStorage.remove(phone);
            return ResponseEntity.status(HttpStatus.GONE).body(Map.of(
                    "ok", false, "message", "인증번호가 만료되었습니다. 다시 전송해 주세요."
            ));
        }

        if (!entry.code().equals(inputCode)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "ok", false, "message", "인증번호가 일치하지 않습니다."
            ));
        }

        // 일회용: 성공 시 즉시 삭제
        codeStorage.remove(phone);

        // (선택) 이후 요청에서 쓸 verificationToken 발급
        String token = "sms-" + phone + "-" + Instant.now().toEpochMilli();

        return ResponseEntity.ok(Map.of(
                "ok", true,
                "message", "인증 성공",
                "verificationToken", token
        ));
    }
}
