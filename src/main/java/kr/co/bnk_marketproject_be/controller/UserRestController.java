package kr.co.bnk_marketproject_be.controller;

import kr.co.bnk_marketproject_be.dto.UserDTO;
import kr.co.bnk_marketproject_be.service.UserService;
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
@RequestMapping("/user")
public class UserRestController {

    private final UserService userService;

    // POST /user/register : 회원가입
    @PostMapping("/register")
    public ResponseEntity<Map<String,Object>> register(@RequestBody UserDTO userDTO) {
        log.info("API register request user_id={}", userDTO.getUserId());
        try {
            userService.register(userDTO);
            return ResponseEntity.ok(Map.of("success", true));
        }
        catch (Exception e) {
            log.error("Register failed", e);
            return ResponseEntity.badRequest().body(Map.of("ok", false, "error", e.getMessage()));
        }
    }

    // GET /user/check-id?user_id=xxx
    @GetMapping("/check-id")
    public ResponseEntity<Map<String, Boolean>> checkId(@RequestParam("user_id") String userId) {
        boolean available = !userService.existsByUserId(userId);
        return ResponseEntity.ok(Map.of("available", available));
    }

    // GET /user/check-email?email=xxx
    @GetMapping("/check-email")
    public ResponseEntity<Map<String, Boolean>> checkEmail(@RequestParam("email") String email) {
        boolean available = !userService.existsByEmail(email);
        return ResponseEntity.ok(Map.of("available", available));
    }

    // GET /user/check-phone?phone=xxx
    @GetMapping("/check-phone")
    public ResponseEntity<Map<String, Boolean>> checkPhone(@RequestParam("phone") String phone) {
        boolean available = !userService.existsByPhone(phone);
        return ResponseEntity.ok(Map.of("available", available));
    }


    @PostMapping("/find-id")
    public ResponseEntity<?> findUserId(@RequestBody Map<String, String> payload) {
        String name = payload.get("name");
        String method = payload.get("method");
        String email = payload.get("email");
        String phone = payload.get("phone");

        Optional<UserDTO> result = userService.findUserId(name, method, email, phone);

        if (result.isPresent()) {
            UserDTO user = result.get();
            Map<String, Object> res = new HashMap<>();
            res.put("ok", true);
            res.put("userId", user.getUserId());
            res.put("name", user.getName());
            res.put("email", user.getEmail());
            res.put("created_at", user.getCreated_at());
            return ResponseEntity.ok(res);
        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("ok", false, "message", "해당 정보로 가입된 사용자를 찾을 수 없습니다."));
        }
    }




}
