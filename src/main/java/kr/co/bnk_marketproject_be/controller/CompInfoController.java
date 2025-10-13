package kr.co.bnk_marketproject_be.controller;

import kr.co.bnk_marketproject_be.dto.CompInfoDTO;
import kr.co.bnk_marketproject_be.service.CompInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class CompInfoController {

    private final CompInfoService compInfoService;

    @GetMapping("/compinfo/compinfo/main")
    public String mainList(Model model){
        List<CompInfoDTO> dtoList = compInfoService.selectFiveCompInfo();
        model.addAttribute("dtoList",dtoList);
        return "compInfo/compInfo_main";
    }

    @GetMapping("/compinfo/compinfo/culture")
    public String cultureList(Model model){
        return "compInfo/compInfo_culture";
    }

    // AdminEmployController에 회사소개_채용 넣어놓음

    @GetMapping("/compinfo/compinfo/media")
    public String mediaList(Model model){
        return "compInfo/compInfo_media";
    }

    @GetMapping("/compinfo/compinfo/news")
    public String newsList(Model model){
        List<CompInfoDTO> dtoList = compInfoService.selectAllCompInfo();
        model.addAttribute("dtoList",dtoList);
        return "compInfo/compInfo_news";
    }
}
