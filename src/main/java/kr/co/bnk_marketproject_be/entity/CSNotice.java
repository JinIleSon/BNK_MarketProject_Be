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

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_BOARD")
    private Long id;

    @Column(name = "BOARD_TYPE", length = 100)
    private String boardType;

    @Column(name = "TITLE", length = 200)
    private String title;

    @Column(name = "CONTENT")
    private String content;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        if (this.boardType == null) this.boardType = "NOTICE";
    }
}
