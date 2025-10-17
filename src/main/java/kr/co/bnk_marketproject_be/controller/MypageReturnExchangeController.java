package kr.co.bnk_marketproject_be.controller;

import kr.co.bnk_marketproject_be.dto.*;
import kr.co.bnk_marketproject_be.service.MypageReturnExchangeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mypage")
public class MypageReturnExchangeController {

    private final MypageReturnExchangeService service;

    // ✅ 반품신청 등록
    @PostMapping("/return")
    public String createReturnRequest(@RequestBody MypageReturnRequestDTO dto) {
        log.info("📦 [반품신청 요청] orderItemId={}, userId={}, reason={}",
                dto.getOrderItemId(), dto.getUserId(), dto.getReasonText());
        service.insertReturnRequest(dto);
        return "반품신청 완료";
    }

    // ✅ 교환신청 등록
    @PostMapping("/exchange")
    public String createExchangeRequest(@RequestBody MypageExchangeRequestDTO dto) {
        log.info("🔁 [교환신청 요청] orderItemId={}, userId={}, reason={}, option={}",
                dto.getOrderItemId(), dto.getUserId(), dto.getReasonText(), dto.getDesiredOption());
        service.insertExchangeRequest(dto);
        return "교환신청 완료";
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
