package kr.co.bnk_marketproject_be.controller;

import kr.co.bnk_marketproject_be.dto.SellerDTO;
import kr.co.bnk_marketproject_be.repository.SellerRepository;
import kr.co.bnk_marketproject_be.repository.UserRepository;
import kr.co.bnk_marketproject_be.service.SellerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/seller")
public class SellerRestController {

    private final SellerService sellerService;
    private final UserRepository userRepo;

    // 판매자 회원가입
    @PostMapping("/register")
    public ResponseEntity<Map<String,Object>> register(@RequestBody SellerDTO sellerDTO) {
        log.info("판매자 회원가입 요청 seller_id={}", sellerDTO.getSellerId());
        try {
            sellerService.register(sellerDTO);
            return ResponseEntity.ok(Map.of("ok", true));
        }
        catch (Exception e) {
            log.error("판매자 등록 실패", e);
            return ResponseEntity.badRequest().body(Map.of("ok", false, "error", e.getMessage()));
        }
    }

    // 아이디 중복확인, user seller 모두 확인
    @GetMapping("/check-id")
    public ResponseEntity<?> checkSellerId(@RequestParam String userId) {
        boolean existsInUser = userRepo.existsByUserId(userId);
        boolean existsInSeller = sellerService.existsBySellerId(userId);

        boolean exists = existsInUser || existsInSeller;  // 둘 중 하나라도 있으면 중복

        return ResponseEntity.ok(Map.of("exists", exists));
    }

    // 이메일 중복확인, user seller 모두 확인
    @GetMapping("/check-email")
    public ResponseEntity<Map<String, Boolean>> checkEmail(@RequestParam("email") String email) {
        boolean existsInUser = userRepo.existsByEmail(email);
        boolean existsInSeller = sellerService.existsByEmail(email);
        boolean available = !(existsInUser || existsInSeller);
        return ResponseEntity.ok(Map.of("available", available));
    }

    // 전화번호 중복확인
    @GetMapping("/check-phone")
    public ResponseEntity<Map<String, Boolean>> checkPhone(@RequestParam("phone") String phone) {
        boolean existsInUser = userRepo.existsByPhone(phone);
        boolean existsInSeller = sellerService.existsByPhone(phone);
        boolean available = !(existsInUser || existsInSeller);
        return ResponseEntity.ok(Map.of("available", available));
    }

    // 아이디 찾기
    @PostMapping("/find-id")
    public ResponseEntity<?> findSellerId(@RequestBody Map<String, String> payload) {
        String name = payload.get("name");
        String method = payload.get("method");
        String email = payload.get("email");
        String phone = payload.get("phone");

        Optional<SellerDTO> result = sellerService.findSellerId(name, method, email, phone);

        if (result.isPresent()) {
            SellerDTO seller = result.get();
            Map<String, Object> res = new HashMap<>();
            res.put("ok", true);
            res.put("sellerId", seller.getSellerId());
            res.put("name", seller.getName());
            res.put("email", seller.getEmail());
            res.put("created_at", seller.getCreated_at());
            return ResponseEntity.ok(res);
        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("ok", false, "message", "해당 정보로 가입된 판매자를 찾을 수 없습니다."));
        }
    }

    // 비밀번호 찾기 (아이디 + 이메일 or 전화번호 확인)
    @PostMapping("/find-password")
    public ResponseEntity<?> findPassword(@RequestBody Map<String, String> req) {
        String sellerId = req.get("sellerId");
        String email = req.get("email");
        String phone = req.get("phone");

        log.info("판매자 비밀번호 찾기 요청: sellerId={}, email={}, phone={}", sellerId, email, phone);

        boolean verified = sellerService.verifySellerForPasswordReset(sellerId, email, phone);

        if (verified) {
            return ResponseEntity.ok(Map.of("ok", true, "message", "판매자 인증 완료"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("ok", false, "message", "일치하는 판매자 정보가 없습니다."));
        }
    }

    // 비밀번호 재설정
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> req) {
        String sellerId = req.get("sellerId");
        String newPassword = req.get("newPassword");

        if (sellerId == null || sellerId.isBlank() || newPassword == null || newPassword.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("ok", false, "message", "잘못된 요청입니다. (sellerId 또는 비밀번호 누락)"));
        }

        try {
            sellerService.resetPassword(sellerId, newPassword);
            return ResponseEntity.ok(Map.of("ok", true, "message", "비밀번호가 변경되었습니다."));
        } catch (Exception e) {
            log.error("판매자 비밀번호 변경 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("ok", false, "message", "비밀번호 변경 중 오류가 발생했습니다."));
        }
    }
}
