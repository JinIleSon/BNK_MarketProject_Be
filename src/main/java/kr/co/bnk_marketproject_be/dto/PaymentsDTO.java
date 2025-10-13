package kr.co.bnk_marketproject_be.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentsDTO {

    private int id;
    private int orders_id;
    private int amount;
    private String method;
    private String status;
    private String paid_at;
}
