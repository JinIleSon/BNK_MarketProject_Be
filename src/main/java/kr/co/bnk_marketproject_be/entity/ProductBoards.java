package kr.co.bnk_marketproject_be.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "PRODUCT_BOARDS")
public class ProductBoards {

    @Id
    private int id;
    private int products_id;
    private int users_id;
    private String type;
    private String title;
    private String content;
    private int rating;
    private String created_at;
}
