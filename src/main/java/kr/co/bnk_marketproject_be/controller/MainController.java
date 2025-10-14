package kr.co.bnk_marketproject_be.controller;

import kr.co.bnk_marketproject_be.service.MainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MainController {
//main 푸시

    private final MainService mainService;

    @GetMapping("/main/main/page")
    public String list(Model model){
        model.addAttribute("newList",       mainService.getProducts(null,        8)); // created_at desc
        model.addAttribute("bestList",      mainService.getProducts("sold",      5)); // 판매수 상위 5
        model.addAttribute("hitList",       mainService.getProducts("hits",      8)); // 조회수 상위 8
        model.addAttribute("recoList",      mainService.getProducts("rating",    8)); // 평점 상위 8
        model.addAttribute("discountList",  mainService.getProducts("discount",  8)); // 할인율 상위 8
        return "main/main_main";
    }
}
