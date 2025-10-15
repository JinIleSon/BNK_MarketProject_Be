package kr.co.bnk_marketproject_be.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MyPageController {

    @GetMapping("/mypage/mypage/main")
    public String mainpage() {
        return "mypage/mypage_main";
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
    public String setupList(){
        return "mypage/mypage_setUp";
    }
    @GetMapping("/mypage/mypage/passwd")
    public String passwdList(){
        return "mypage/mypage_passwd";
    }

}
