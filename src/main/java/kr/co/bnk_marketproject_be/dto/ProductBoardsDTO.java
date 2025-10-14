package kr.co.bnk_marketproject_be.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductBoardsDTO {

    private int id;
    private int products_id;
    private int users_id;
    private String type;
    private String title;
    private String content;
    private int rating;
    private String created_at;

    // ★ 추가: 표시할 로그인 ID
    private String user_id; // users.user_id
}
