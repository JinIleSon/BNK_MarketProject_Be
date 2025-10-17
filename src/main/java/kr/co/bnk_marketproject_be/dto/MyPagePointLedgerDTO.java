package kr.co.bnk_marketproject_be.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyPagePointLedgerDTO {

    private int id;
    private String user_id;
    private LocalDateTime created_at;
    private int sum_point;
    private int total_point;
    private int change_amount;
    private String description;
}
