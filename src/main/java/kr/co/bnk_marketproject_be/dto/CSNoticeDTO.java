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
    private String boardType2;
    private String boardType3;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private String look;
    private String userid;

    // 문의하기_뷰 댓글출력
    private int cid;

    // write hits값 고정
    private int hits;


}