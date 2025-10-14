package kr.co.bnk_marketproject_be.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AdminVersionDTO {

    private Long id;               // PK
    private String versionName;    // 버전 이름 (예: 0.0.1-SNAPSHOT)
    private String author;         // 작성자 ID
    private String changeLog;  // 변경내역
    private LocalDateTime createdAt; // 등록일시 (SYSTIMESTAMP 매핑)
}
