package kr.co.bnk_marketproject_be.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "employ")
public class AdminEmploy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String department;
    private String career;
    private String form;
    private String title;
    private String user_name;
    private String status;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate recruit_from;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate recruit_to;
    @CreationTimestamp
    private LocalDateTime created_at;
    private String note;
}
