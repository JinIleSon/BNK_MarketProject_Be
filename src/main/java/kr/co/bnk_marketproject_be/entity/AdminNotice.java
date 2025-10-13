package kr.co.bnk_marketproject_be.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

@Getter @Setter
@Builder
@NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "BOARD")
@Where(clause = "UPPER(BOARD_TYPE) = 'NOTICE'")
@SequenceGenerator(name = "SEQ_BOARD", sequenceName = "SEQ_BOARD", allocationSize = 1)
public class AdminNotice {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_BOARD")
    private Long id;

    @Column(name="BOARD_TYPE", length=200, nullable = false)
    private String boardType; // 항상 "notice"

    @Column(name="USER_ID", length=200)
    private String userId;

    @Column(name="TITLE", length=200, nullable=false)
    private String title;

    @Column(name="CONTENT")
    private String content;

    @Column(name="CREATED_AT", insertable = false, updatable = false)
    private String createdAt;

    @Column(name="UPDATED_AT")
    private String updatedAt;

    @Column(name="LOOK")
    private Long look;

    @PrePersist
    public void prePersist() {
        if (this.boardType == null) this.boardType = "notice";
    }
}
