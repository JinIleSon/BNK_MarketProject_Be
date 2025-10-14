package kr.co.bnk_marketproject_be.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PointLedgersDTO {

    private int id;
    private String users_id;
    private int change_amount;
    private int balance;
    private int description;
    private String created_at;
    private String board_type;

}
