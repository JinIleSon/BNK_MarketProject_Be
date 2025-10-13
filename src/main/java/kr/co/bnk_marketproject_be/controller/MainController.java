package kr.co.bnk_marketproject_be.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MainController {
//main 푸시합니다
    @GetMapping("/main/main/page")
    public String list(){
        return "main/main_main";
    }
}
