package kr.co.bnk_marketproject_be.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "board")
public class AdminFAQ {

    @Id
    private int id;
    private String board_type;
    private String board_type2;
    private String board_type3;
    private String title;
    private String content;
    private int hits;

    private LocalDateTime created_at;
    @PrePersist
    private void onCreate() {
        if (created_at == null) created_at = LocalDateTime.now();
    }
}
