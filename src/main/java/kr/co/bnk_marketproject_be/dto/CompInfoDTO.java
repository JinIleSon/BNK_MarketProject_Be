package kr.co.bnk_marketproject_be.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompInfoDTO {

    private int id;
    private String title;
    private String url;
    private LocalDateTime created_at;
    private String imgurl;
    private String sub;
    private String description;
    private String classification;
}
