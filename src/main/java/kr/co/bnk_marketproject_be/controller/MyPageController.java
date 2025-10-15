package kr.co.bnk_marketproject_be.controller;

import kr.co.bnk_marketproject_be.dto.UserDTO;
import kr.co.bnk_marketproject_be.service.MyPageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;

    @GetMapping("/mypage/mypage/main")
    public String mainpage() {
        return "mypage/mypage_main";
    }
    @GetMapping("/mypage/mypage/allorder")
    public String allorderList(){
        return "mypage/mypage_allorder";
    }
    @GetMapping("/mypage/mypage/point")
    public String pointList(){
        return "mypage/mypage_point";
    }
    @GetMapping("/mypage/mypage/coupon")
    public String couponList(){
        return "mypage/mypage_coupon";
    }
    @GetMapping("/mypage/mypage/review")
    public String reviewList(){
        return "mypage/mypage_review";
    }
    @GetMapping("/mypage/mypage/ask")
    public String askList(){
        return "mypage/mypage_ask";
    }
    @GetMapping("/mypage/mypage/setup")
    public String setupList(Model  model, Principal principal){

        String userId = principal.getName();
        UserDTO userDTO = myPageService.selectUser(userId);

        log.info("userId = {}", userId);
        userDTO.setUserId(userId);
        log.info("userDTO = {}", userDTO);

        model.addAttribute("userDTO", userDTO);
        return "mypage/mypage_setUp";
    }

    @PostMapping("/mypage/mypage/setup")
    public String modify(UserDTO userDTO, Model  model, Principal principal, @RequestParam(required = false) Integer code){
        model.addAttribute("userDTO", userDTO);
        model.addAttribute("code", code);

        log.info("userDTO = {}", userDTO);
        log.info("code = {}", code);

        return "/mypage/mypage_passwd";
    }

    @GetMapping("/mypage/mypage/passwd")
    public String passwdList(Principal principal, @RequestParam(required = false) Integer code, Model  model){

        String userId = principal.getName();
        UserDTO userDTO = myPageService.selectUser(userId);

        log.info("userId = {}", userId);
        log.info("userDTO = {}", userDTO);

        model.addAttribute("userDTO", userDTO);
        model.addAttribute("code", code);

        return "mypage/mypage_passwd";
    }

    @PostMapping("/mypage/mypage/passwd")
    public String codePasswd(Principal principal, @RequestParam(required = false) Integer code, Model  model){

        String userId = principal.getName();
        UserDTO userDTO = myPageService.selectUser(userId);

        log.info("userId = {}", userId);
        log.info("userDTO = {}", userDTO);

        model.addAttribute("userDTO", userDTO);
        model.addAttribute("code", code);

        if(code.equals(1)){
            return "redirect:/user/reset-password";
        }
        else if(code.equals(2)){

        }
        else if(code.equals(3)){

        }

        return "mypage/mypage_passwd";
    }

}
