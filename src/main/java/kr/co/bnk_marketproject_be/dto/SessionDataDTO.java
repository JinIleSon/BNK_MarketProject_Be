package kr.co.bnk_marketproject_be.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Setter
@Getter
@Data
@Component
@SessionScope // 각 세션(사용자)마다 생성
public class SessionDataDTO {
    // 이메일 code
    private String code;
    private Long codeTimestamp;
    private boolean verified = false;

    // SMS 코드
    private String smsCode;
    private Long smsCodeTimestamp;
    private boolean smsVerified = false;

}
