package kr.co.bnk_marketproject_be.service;

import kr.co.bnk_marketproject_be.entity.User;
import kr.co.bnk_marketproject_be.security.MyUserDetails;
import kr.co.bnk_marketproject_be.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserService userService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest req) throws OAuth2AuthenticationException {
        OAuth2User delegate = super.loadUser(req);
        Map<String, Object> attrs = delegate.getAttributes();

        String email = (String) attrs.get("email");
        // DB에서 유저 찾거나 신규 생성 (provider=GOOGLE, role 기본 "user" 보정 등)
        User user = userService.upsertGoogleUser(email, attrs);

        // 통합 Principal로 반환 (attributes 포함)
        return MyUserDetails.builder()
                .user(user)
                .attributes(Collections.unmodifiableMap(new HashMap<>(attrs)))
                .build();
    }
}
