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


    public PageResponseProductDTO(PageRequestProductDTO req, List<T> dtoList, int total) {
        this.dtoList = (dtoList == null) ? List.of() : dtoList;
        this.size = Math.max(1, req.getSize());
        this.pg   = Math.max(1, req.getPg());
        this.total = Math.max(0, total);

        int last = (int) Math.ceil(this.total / (double) this.size); // 마지막 페이지

        // 데이터가 없을 때: 1페이지로 고정하고 페이지 블록을 1~1로 보정
        if (last == 0) {
            this.pg = 1;
            this.start = 1;
            this.end = 1;
            this.prev = false;
            this.next = false;
            return;
        }

        // 요청 pg가 범위를 벗어나면 보정
        if (this.pg > last) this.pg = last;

        this.end = (int) (Math.ceil(this.pg / 10.0)) * 10;   // 10개 단위 블록
        this.start = Math.max(1, this.end - 9);
        if (this.end > last) this.end = last;

        this.prev = this.start > 1;
        this.next = this.end < last;
    }

}
