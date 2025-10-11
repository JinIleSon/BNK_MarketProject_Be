package kr.co.bnk_marketproject_be.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Transient;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRegDTO {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int product_code;
    private int sellers_id;
    private int categories_id;
    private String product_name;
    private String description;
    private int price;
    private int stock;
    private String status;
    @CreationTimestamp
    private LocalDateTime created_at;
    @CreationTimestamp
    private LocalDateTime updated_at;
    private int discount;
    private int hits;
    private int point;
    private String sub_description;
    private String make;
    private int delichar;
    private String dutyfree;
    private String recpub;
    private String busdiv;
    private String prodcondition;
    private String origin;

    @Transient
    private int second;
    @Transient
    private String option_name;
    @Transient
    private MultipartFile file;
    @Transient
    private String url;
}
