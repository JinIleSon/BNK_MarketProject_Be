package kr.co.bnk_marketproject_be.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductCartDTO {
    private List<OrderPageLineRowDTO> items;
    private OrderPageSummaryDTO summary;

    // 체크박스/일괄삭제 등 UX 제어용
    @Builder.Default
    private boolean selectable = true;
}
