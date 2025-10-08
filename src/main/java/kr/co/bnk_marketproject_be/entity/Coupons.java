package kr.co.bnk_marketproject_be.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "coupons")
public class Coupons {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int users_id;
    private String code;
    private String discount_type;
    private int discount_value;
    private String company;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDateTime valid_from;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDateTime valid_to;

    private String status;
    private String coupon_name;
    private int issuecount;
    private int usercount;

    @CreationTimestamp
    private LocalDateTime created_at;

    // 추가 필드 - name(users) - 발급자
    @Transient
    private String name;

    // 추가 필드 - 유의사항
    private String note;

}
