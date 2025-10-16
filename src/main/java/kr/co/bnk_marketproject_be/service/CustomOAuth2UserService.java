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

        String registrationId = req.getClientRegistration().getRegistrationId();
        String email = null;

        if ("google".equals(registrationId)) {
            email = (String) attrs.get("email");
        } else if ("kakao".equals(registrationId)) {
            Object kakaoAccountObj = attrs.get("kakao_account");
            if (kakaoAccountObj instanceof Map) {
                Map<String, Object> kakaoAccount = (Map<String, Object>) kakaoAccountObj;
                email = (String) kakaoAccount.get("email");
                attrs = kakaoAccount; // ✅ 실제 유효한 속성 map으로 교체
            } else {
                // ✅ fallback: 루트에서 id라도 가져오기
                email = null;
            }
           // 네이버는 response 안에 실제 유저 데이터가 있어서 구글 카카오랑 다르게 attrs = response 이걸로
        } else if ("naver".equals(registrationId)) {
            Map<String, Object> response = (Map<String, Object>) attrs.get("response");
            if (response != null) {
                email = (String) response.get("email");
                attrs = response;
            }
        }

        // 유저 등록 또는 조회
        User user = userService.upsertOAuthUser(registrationId, email, attrs);

        return MyUserDetails.builder()
                .user(user)
                .attributes(Collections.unmodifiableMap(new HashMap<>(attrs)))
                .build();
    }

        // ✅ 공통 Principal 반환
//        return MyUserDetails.builder()
//                .user(user)
//                .attributes(Collections.unmodifiableMap(profileAttrs))
//                .build();
//    }
}
