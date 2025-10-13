package kr.co.bnk_marketproject_be.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductImagesDTO {
    private int id;
    private int products_id;
    private String url;
    private String is_main;
    @CreationTimestamp
    private LocalDateTime created_at;
    private int product_code;
}
