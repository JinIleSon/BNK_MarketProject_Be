package kr.co.bnk_marketproject_be.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "BOARD")
@Where(clause = "UPPER(BOARD_TYPE) = 'NOTICE'")
public class CSNotice {

    // CSNoticeDTO랑 연동 "_"를 없애기 위해서 불러오기용.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "BOARD_TYPE")
    private String boardType;

    @Column(name = "BOARD_TYPE2")
    private String boardType2;

    @Column(name = "BOARD_TYPE3")
    private String boardType3;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "USER_ID")
    private String userid;

    private String title;
    private String content;
    private int look;

}
