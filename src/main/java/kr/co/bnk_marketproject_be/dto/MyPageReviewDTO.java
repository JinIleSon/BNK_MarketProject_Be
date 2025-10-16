package kr.co.bnk_marketproject_be.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyPageReviewDTO {

    private int id;
    private String user_id;
    private int product_code;
    private String product_name;
    private String content;
    private int rating;
    private LocalDateTime created_at;
    private int pid;

}
