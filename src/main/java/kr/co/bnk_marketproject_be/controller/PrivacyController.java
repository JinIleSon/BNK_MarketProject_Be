package kr.co.bnk_marketproject_be.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
public class PrivacyController {

    @GetMapping("/policy/policy/terms")
    public String list(){
        return "policy/policy_terms";
    }
}
