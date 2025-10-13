package kr.co.bnk_marketproject_be.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/* ////////////////////////////////////////////////////////////////
*      윈도우 파워쉘에서 전송계층 핑퐁테스트를 위한 controller입니다.
*      이메일 인증시 네트워트 테스트 했고 추후 다른 테스트를 위해서
*      놔둬주시기 바랍니다. 누군가 핑써서 팡으로 네이밍함
//////////////////////////////////////////////////////////////// */

@Slf4j
@RestController
public class PangController {

    @GetMapping("/email/pang")
    public String pang() {
        log.info(">> /email/pang called");
        return "pooong";
    }
}
