package kr.co.bnk_marketproject_be.controller;

import kr.co.bnk_marketproject_be.dto.UserDTO;
import kr.co.bnk_marketproject_be.entity.User;
import kr.co.bnk_marketproject_be.repository.UserRepository;
import kr.co.bnk_marketproject_be.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

// user seller 회원가입, 로그인 관련 기본 화면 컨트롤러

@Slf4j
@RequiredArgsConstructor
@Controller
public class MemberController {

    private final UserRepository userRepository;
    private final UserService userService;

    @GetMapping("/member/join")
    public String member_join(){
        return "member/member_join";
    }

    @GetMapping("/member/login")
    public String member_login(){
        System.out.println("✅ MemberController.member_login() 호출됨");
        return "member/member_login";
    }

    @GetMapping("/member/register")
    public String member_register(){
        return "member/member_register";
    }

    @GetMapping("/member/signup")
    public String member_signup(){
        return "member/member_signup";
    }

    @GetMapping("/member/changepassword")
    public String member_changepassword(Model model, UserDTO userDTO, Principal principal){
        String userId = principal.getName();
        userDTO.setUserId(userId);
        log.info("userId = {}", userId);
        model.addAttribute("userDTO", userDTO);
        log.info("userDTO = {}", userDTO);
        return "member/member_change_password";
    }
    // 푸시용
    @PostMapping("/member/changepassword")
    public String member_changepassword(Model model, UserDTO userDTO){
        User user = userRepository.findByUserId(userDTO.getUserId());

        if(user == null){
            model.addAttribute("error", "해당 사용자를 찾을 수 없습니다.");
            return "error";
        }

        // 비밀번호 암호화
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());

        // 암호화된 비밀번호 저장
        user.setPassword(encodedPassword);
        userRepository.save(user);

        // 완료 후 리다이렉트 또는 성공 페이지 이동
        model.addAttribute("message", "비밀번호가 성공적으로 변경되었습니다.");
        return "redirect:/main/main/page"; // 원하는 페이지로 변경
    }

    @GetMapping("/member/findpassword")
    public String member_findpassword(){
        return "member/member_find_password";
    }

    @GetMapping("/member/findresultId")
    public String member_findresultId(){
        return "member/member_find_resultId";
    }

    @GetMapping("/member/finduserId")
    public String member_finduserId(){
        return "member/member_find_userId";
    }


    // seller
    @GetMapping("/member/registerSeller")
    public String member_registerSeller(){
        return "member/member_registerSeller";
    }

    @GetMapping("/member/signupseller")
    public String member_signupSeller(){
        return "member/member_signup_seller";
    }

    // 임시로 맵핑한것
    @GetMapping("/main/mainpage")
    public String mainPage() {
        return "main/main_main"; // ✅ templates/main/main_main.html 렌더링
    }

    @Getter @Setter
    public static class IssueTempPasswordRequest {   // ★ static 추가 + public 권장
        private String userId;
        private String method; // "email" or "phone"
        private String email;
        private String phone;
    }

    @Getter
    @AllArgsConstructor
    public static class IssueTempPasswordResponse {  // ★ static 추가 + public 권장
        private boolean ok;
        private String message;
        private String tempPassword;
    }

    @PostMapping("/member/issue-temp-password")
    @ResponseBody
    public ResponseEntity<IssueTempPasswordResponse> issueTempPassword(
            @RequestBody IssueTempPasswordRequest req) {

        // 1) 입력 체크
        if (req.getUserId() == null || req.getUserId().isBlank()) {
            return ResponseEntity.badRequest()
                    .body(new IssueTempPasswordResponse(false, "userId가 필요합니다.", null));
        }
        if (!"email".equals(req.getMethod()) && !"phone".equals(req.getMethod())) {
            return ResponseEntity.badRequest()
                    .body(new IssueTempPasswordResponse(false, "지원하지 않는 방식입니다.", null));
        }

        // 2) 사용자 검증 (아이디 + 이메일/휴대폰 매칭)
        boolean verified = userService.verifyUserForPasswordReset(
                req.getUserId(),
                "email".equals(req.getMethod()) ? req.getEmail() : null,
                "phone".equals(req.getMethod()) ? req.getPhone() : null
        );
        if (!verified) {
            return ResponseEntity.badRequest()
                    .body(new IssueTempPasswordResponse(false, "회원 정보가 일치하지 않습니다.", null));
        }

        // 3) 임시 비밀번호 생성 및 DB 저장 (해시로)
        String temp = userService.issueTempPasswordAndReturnPlain(req.getUserId());

        // 4) 응답(평문 임시비번 포함)  ※ 알럿 노출용
        return ResponseEntity.ok(new IssueTempPasswordResponse(true, "issued", temp));
    }

}
