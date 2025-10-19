package kr.co.bnk_marketproject_be.controller;

import kr.co.bnk_marketproject_be.dto.*;
import kr.co.bnk_marketproject_be.service.MypageReturnExchangeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mypage")
public class MypageReturnExchangeController {

    private final MypageReturnExchangeService service;

    // ✅ 반품신청 등록
    @PostMapping(value = "/return", consumes = "multipart/form-data")
    public String createReturnRequest(
            @RequestParam("order_item_id") Long orderItemId,
            @RequestParam(value = "user_id", required = false) Long userId,
            @RequestParam("reason_text") String reasonText,
            @RequestParam("reason_code") String reasonCode,
            @RequestParam("status") String status,
            @RequestParam(value = "evidence_urls", required = false) MultipartFile evidenceFile
    ) {
        log.info("📦 [반품신청 요청] orderItemId={}, userId={}, reason={}, code={}, file={}",
                orderItemId, userId, reasonText, reasonCode,
                evidenceFile != null ? evidenceFile.getOriginalFilename() : "없음");

        MypageReturnRequestDTO dto = MypageReturnRequestDTO.builder()
                .orderItemId(orderItemId)
                .userId(userId)
                .reasonText(reasonText)
                .reasonCode(reasonCode)
                .status(status)
                .evidenceUrls(evidenceFile != null ? evidenceFile.getOriginalFilename() : null)
                .build();

        service.insertReturnRequest(dto);
        return "반품신청 완료";
    }


    // ✅ 교환신청 등록
    @PostMapping(value = "/exchange", consumes = "multipart/form-data")
    public ResponseEntity<String> createExchangeRequest(
            @RequestParam("order_item_id") Long orderItemId,
            @RequestParam(value = "user_id", required = false) Long userId,
            @RequestParam("reason_text") String reasonText,
            @RequestParam("desired_option") String desiredOption,
            @RequestParam("reason_code") String reasonCode,
            @RequestParam("status") String status,
            @RequestParam(value = "evidence_urls", required = false) MultipartFile evidenceFile
    ) {
        try {
            log.info("🔁 [교환신청 요청] orderItemId={}, userId={}, desiredOption={}, reasonCode={}, status={}",
                    orderItemId, userId, desiredOption, reasonCode, status);

            String evidenceUrl = null;
            if (evidenceFile != null && !evidenceFile.isEmpty()) {
                evidenceUrl = evidenceFile.getOriginalFilename();
            }

            MypageExchangeRequestDTO dto = new MypageExchangeRequestDTO();
            dto.setOrderItemId(orderItemId);
            dto.setUserId(userId);
            dto.setReasonText(reasonText);
            dto.setDesiredOption(desiredOption);
            dto.setReasonCode(reasonCode);
            dto.setStatus(status);
            dto.setEvidenceUrls(evidenceUrl);

            log.info("📋 [Controller] 교환신청 DTO 생성 완료: {}", dto);
            service.insertExchangeRequest(dto);

            return ResponseEntity.ok("success");
        } catch (Exception e) {
            log.error("❌ [교환신청 오류]", e);
            return ResponseEntity.internalServerError().body("fail");
        }
    }





    // ✅ 반품신청 조회
    @GetMapping("/return/{userId}")
    public Object getReturnList(@PathVariable Long userId) {
        log.info("📋 [반품신청 목록 조회] userId={}", userId);
        return service.findReturnList(userId);
    }

    // ✅ 교환신청 조회
    @GetMapping("/exchange/{userId}")
    public Object getExchangeList(@PathVariable Long userId) {
        log.info("📋 [교환신청 목록 조회] userId={}", userId);
        return service.findExchangeList(userId);
    }
}
