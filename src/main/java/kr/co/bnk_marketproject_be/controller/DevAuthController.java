package kr.co.bnk_marketproject_be.controller;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class DevAuthController {

    @PostMapping("/dev/login-as")
    @ResponseBody
    public String loginAs(@RequestParam int uid) {
        var auth = new UsernamePasswordAuthenticationToken(
                String.valueOf(uid), "N/A",
                List.of(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContextHolder.getContext().setAuthentication(auth);
        return "OK (as uid=" + uid + ")";
    }
}
