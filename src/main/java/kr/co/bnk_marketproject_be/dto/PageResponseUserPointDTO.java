package kr.co.bnk_marketproject_be.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PageResponseUserPointDTO {

    private List<AdminPointDTO> dtoList;

    private String boardType; // 게시판 종류
    private int pg;
    private int size;
    private int total;
    private int startNo;
    private int start, end;
    private boolean prev, next; // 이전, 다음 버튼

    private String searchType;
    private String keyword;

    @Builder
    public PageResponseUserPointDTO(PageRequestDTO pageRequestDTO, List<AdminPointDTO> dtoList, int total) {

        this.boardType = pageRequestDTO.getBoardType();
        this.pg = pageRequestDTO.getPg();
        this.size = pageRequestDTO.getSize();
        this.total = total;
        this.dtoList = dtoList;

        this.startNo = total - ((pg - 1) * size);
        this.end = (int)(Math.ceil(this.pg / 10.0)) * 10;
        this.start = this.end - 9;

        int last = (int)(Math.ceil(total / (double) size));
        this.end = Math.min(end, last);
        this.prev = this.start > 1;
        this.next = total > this.end * this.size;

        this.searchType = pageRequestDTO.getSearchType();
        this.keyword = pageRequestDTO.getKeyword();
    }

}
