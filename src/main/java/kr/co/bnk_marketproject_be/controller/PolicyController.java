package kr.co.bnk_marketproject_be.controller;

import kr.co.bnk_marketproject_be.service.PolicyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
public class PolicyController {

    private final PolicyService policyService;

    @GetMapping("/policy/policy/terms")
    public String list(Model model) {
        model.addAttribute("dtoList", policyService.selectAllPolicies());
        return "policy/policy_terms";
    }
}
