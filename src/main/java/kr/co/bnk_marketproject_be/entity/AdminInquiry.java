package kr.co.bnk_marketproject_be.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Getter @Setter
@Builder
@NoArgsConstructor @AllArgsConstructor
@Entity
@Table(schema = "NICHIYA", name = "BOARD")
@Where(clause = "UPPER(BOARD_TYPE) = 'INQUIRY'")
@SequenceGenerator(name = "SEQ_BOARD", sequenceName = "SEQ_BOARD", allocationSize = 1)
public class AdminInquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_BOARD")
    private Long id;

    @Column(name="BOARD_TYPE", length=200, nullable = false)
    private String boardType; // "inquiry"
    private String board_type2;
    private String board_type3;

    @Column(name="TITLE", length=200, nullable=false)
    private String title;

    @Column(name="USER_ID", length=200)
    private String userId;

    // @Lob 제거 (DB는 VARCHAR2(200))
    @Column(name="CONTENT")
    private String content;

    @Column(name="TEL", length=50)
    private String tel;

    @Column(name="EMAIL", length=200)
    private String email;

    @Column(name="CREATED_AT", insertable = false, updatable = false)
    private LocalDateTime createdAt;


    @Column(name="UPDATED_AT", insertable = false, updatable = false)
    private String updatedAt;

    private int hits;

    @Column(name="LOOK")
    private Long look;

    @PrePersist
    public void prePersist() {
        if (this.boardType == null) this.boardType = "inquiry";
    }
}
