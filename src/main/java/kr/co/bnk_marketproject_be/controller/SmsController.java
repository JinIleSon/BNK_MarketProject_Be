package kr.co.bnk_marketproject_be.controller;

import kr.co.bnk_marketproject_be.service.SolapiSmsService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/sms")
public class SmsController {

    private final SolapiSmsService smsService;
    private final Map<String, String> codeStorage = new HashMap<>();

    public SmsController(SolapiSmsService smsService) {
        this.smsService = smsService;
    }

    @PostMapping("/send")
    public String sendSms(@RequestBody Map<String, String> request) {
        String phoneNumber = request.get("phoneNumber");
        try {
            String code = smsService.sendVerificationCode(phoneNumber);

            // ✅ 코드 임시 저장 (폰번호 기준)
            codeStorage.put(phoneNumber, code);

            // ✅ 인텔리J 콘솔 로그 추가
            System.out.println("📤 [SMS 전송 성공] 수신번호: " + phoneNumber + ", 인증번호: " + code);

            return "인증번호 전송 완료: " + code;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ [SMS 전송 실패] 수신번호: " + phoneNumber + ", 이유: " + e.getMessage());
            return "전송 실패: " + e.getMessage();
        }
    }

    @PostMapping("/verify")
    public String verifyCode(@RequestBody Map<String, String> request) {
        String inputCode = request.get("code");
        String phoneNumber = request.get("phoneNumber"); // 프론트에서 같이 보낼 예정
        String savedCode = codeStorage.get(phoneNumber);

        if (savedCode != null && savedCode.equals(inputCode)) {
            System.out.println("✅ [SMS 인증 성공] 입력한 코드: " + inputCode);
            return "인증 성공 ✅";

        } else {
            System.out.println("⚠️ [SMS 인증 실패] 입력한 코드: " + inputCode);
            return "인증번호가 일치하지 않습니다.";
        }
    }
}
