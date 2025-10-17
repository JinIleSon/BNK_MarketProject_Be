package kr.co.bnk_marketproject_be.controller;

import kr.co.bnk_marketproject_be.dto.UserDTO;
import kr.co.bnk_marketproject_be.entity.User;
import kr.co.bnk_marketproject_be.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

// user seller 회원가입, 로그인 관련 기본 화면 컨트롤러

@Slf4j
@RequiredArgsConstructor
@Controller
public class MemberController {

    private final UserRepository userRepository;

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

}
