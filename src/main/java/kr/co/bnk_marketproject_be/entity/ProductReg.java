package kr.co.bnk_marketproject_be.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "products")
public class ProductReg {

    @Id
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
