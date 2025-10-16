package kr.co.bnk_marketproject_be.security;

import kr.co.bnk_marketproject_be.entity.User;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;


@Data
@Builder(toBuilder = true)
public class MyUserDetails implements UserDetails, OAuth2User {

    private User user;

    // // OAuth2 attributes 저장용 (구글 프로필 등)
    @Builder.Default
    private Map<String, Object> attributes =  Collections.emptyMap();

    //    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        // 계정 목록 리스트 생성, 인가 처리에 사용
//        List<GrantedAuthority> authorities = new ArrayList<>();
//        // ROLE_ 접두어 제거했으므로 그대로 넣기
//        authorities.add(new SimpleGrantedAuthority(user.getRole())); // "admin" / "seller" / "user"
//        return authorities;
//    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // DB에 null이 올 수도 있으니 기본값 보정
        String role = (user.getRole() != null && !user.getRole().isBlank()) ? user.getRole() : "user";
        return List.of(new SimpleGrantedAuthority(role)); // "admin" / "seller" / "user"
    }



    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        if (user.getUserId() != null && !user.getUserId().isBlank()) return user.getUserId();
        if (user.getEmail() != null && !user.getEmail().isBlank()) return user.getEmail();
        return String.valueOf(user.getId());
        //return user.getUserId();
    }

    @Override
    public boolean isAccountNonExpired() {
        // 계정 만료 여부(true:만료안됨, false: 만료)
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // 비밀번호 만료 여부
        return true;
    }

    @Override
    public boolean isEnabled() {
        // 계정 활성화 여부
        return true;  // user에 활성화 상태가 없으면 일단 true로..
        // 혹은 user.getStatus() == Status.ACTIVE 등으로 맵핑
    }

    // setter (@Data면 세터 자동 생성됨)
    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes != null
                ? java.util.Collections.unmodifiableMap(new java.util.HashMap<>(attributes))
                : java.util.Collections.emptyMap();
    }

    // --- OAuth2User 구현 ---
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        // 구글 고유 ID(sub) 우선, 없으면 이메일/ID
        Object sub = attributes.get("sub");
        if (sub != null) return sub.toString();
        if (user.getEmail() != null && !user.getEmail().isBlank()) return user.getEmail();
        if (user.getUserId() != null && !user.getUserId().isBlank()) return user.getUserId();
        //if (user.getEmail() != null) return user.getEmail();
        return String.valueOf(user.getId());
    }
}