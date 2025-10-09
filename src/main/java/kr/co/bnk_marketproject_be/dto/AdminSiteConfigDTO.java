package kr.co.bnk_marketproject_be.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminSiteConfigDTO {
    private Integer id;
    private String siteTitle;
    private String siteSubtitle;
    private String version;        // VERSION
    private LocalDateTime updatedAt; // UPDATED_AT
    private String headerLogo;
    private String footerLogo;
    private String favicon;
    private String companyName;     // 상호명
    private String ceoName;         // 대표이사
    private String bizRegNo;        // 사업자등록번호
    private String mailOrderNo;     // 통신판매업신고
    private String addrBase;        // 기본주소
    private String addrDetail;      // 상세주소
    private String csTel;           // 고객센터 전화번호
    private String csHours;         // 업무시간
    private String csEmail;         // 고객센터 이메일
    private String efinDisputeTel;  // 전자금융거래 분쟁담당
    private String copyrightText;   // 카피라이트
    private String termsBuyer;     // TERMS_BUYER
    private String termsSeller;    // TERMS_SELLER
    private String termsFinance;   // TERMS_FINANCE
    private String termsLocation;  // TERMS_LOCATION
    private String termsPrivacy;   // TERMS_PRIVACY
}
