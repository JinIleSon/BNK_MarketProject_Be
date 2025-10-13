package kr.co.bnk_marketproject_be.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
public class CompInfoController {

    @GetMapping("/compinfo/compinfo/main")
    public String mainList(Model model){
        return "compInfo/compInfo_main";
    }

    @GetMapping("/compinfo/compinfo/culture")
    public String cultureList(Model model){
        return "compInfo/compInfo_culture";
    }

    @GetMapping("/compinfo/compinfo/emp")
    public String empList(Model model){
        return "compInfo/compInfo_emp";
    }

    @GetMapping("/compinfo/compinfo/media")
    public String mediaList(Model model){
        return "compInfo/compInfo_media";
    }

    @GetMapping("/compinfo/compinfo/news")
    public String newsList(Model model){
        return "compInfo/compInfo_news";
    }
}
