package kr.co.bnk_marketproject_be.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// user seller 회원가입, 로그인 관련 기본 화면 컨트롤러

@Slf4j
@RequiredArgsConstructor
@Controller
public class MemberController {

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
    public String member_changepassword(){
        return "member/member_change_password";
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
