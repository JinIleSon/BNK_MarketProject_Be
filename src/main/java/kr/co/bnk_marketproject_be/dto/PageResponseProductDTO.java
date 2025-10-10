package kr.co.bnk_marketproject_be.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageResponseProductDTO<T> {

    private List<T> dtoList;  // 실제 데이터 리스트
    private int pg;           // 현재 페이지
    private int size;         // 페이지당 데이터 수
    private int total;        // 전체 데이터 수
    private int start;        // 시작 페이지 번호
    private int end;          // 끝 페이지 번호
    private boolean prev;     // 이전 버튼 여부
    private boolean next;     // 다음 버튼 여부


    public PageResponseProductDTO(PageRequestProductDTO pageRequestProductDTO, List<T> dtoList, int total) {
        this.dtoList = dtoList;
        this.total = total;
        this.pg = pageRequestProductDTO.getPg();
        this.size = pageRequestProductDTO.getSize();

        this.end = (int) (Math.ceil(this.pg / 10.0)) * 10;
        this.start = this.end - 9;
        int last = (int) Math.ceil(total / (double) size);

        if (last < end) this.end = last;

        this.prev = this.start > 1;
        this.next = total > this.end * this.size;
    }

}
