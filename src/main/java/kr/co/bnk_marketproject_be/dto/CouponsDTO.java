package kr.co.bnk_marketproject_be.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Transient;
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
public class CouponsDTO {

    private int id;
    private int users_id;
    private String code;
    private String discount_type;
    private int discount_value;
    private String company;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime valid_from;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
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
