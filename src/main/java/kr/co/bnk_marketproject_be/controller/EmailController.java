package kr.co.bnk_marketproject_be.controller;

import kr.co.bnk_marketproject_be.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/email")
public class EmailController {

    private final EmailService emailService;

    // POST /email/send
    @PostMapping("/send")
    public ResponseEntity<Map<String, Object>> sendCode(@RequestBody Map<String, String> jsonData) {
        String email = jsonData.get("email");
        log.info(">>> EmailController.sendCode called, email={}", email);

        try {
            emailService.sendCode(email);
            return ResponseEntity.ok(Map.of("sent", true));
        } catch (Exception e) {
            log.error(">>> EmailController.sendCode failed", e);
            return ResponseEntity.status(500).body(Map.of("sent", false, "error", e.getMessage()));
        }
    }

    // POST /email/code
    @PostMapping("/code")
    public ResponseEntity<Map<String, Boolean>> verify(@RequestBody Map<String, String> jsonData) {
        String code = jsonData.get("code");
        log.info(">>> EmailController.verify called, code={}", code);

        //boolean result = emailService.verifyCode(code);
        boolean ok = emailService.verifyCode(code);
        return ResponseEntity.ok(Map.of("isMatched", ok));
    }
}
