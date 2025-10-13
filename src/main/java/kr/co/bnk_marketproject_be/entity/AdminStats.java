package kr.co.bnk_marketproject_be.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "ORDERS")   // 실제 테이블명 매핑
public class AdminStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Oracle이면 SEQUENCE 전략 쓰는게 맞을 수도 있음
    @Column(name = "ID")
    private Long id;

    @Column(name = "USERS_ID")
    private Long userId;

    @Column(name = "STATUS", length = 20)
    private String status;

    @Column(name = "TOTAL_AMOUNT")
    private Long totalAmount;

    @CreationTimestamp
    @Column(name = "CREATED_AT", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;
}
