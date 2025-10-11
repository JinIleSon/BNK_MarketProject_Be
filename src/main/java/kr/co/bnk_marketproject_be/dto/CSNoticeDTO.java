package kr.co.bnk_marketproject_be.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CSNoticeDTO {
    private Long id;
    private String boardType;
    private String title;
    private String content;
    private LocalDateTime createdAt;
}