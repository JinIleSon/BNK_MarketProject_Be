package kr.co.bnk_marketproject_be.service.impl;

import kr.co.bnk_marketproject_be.dto.AdminInquiryDTO;
import kr.co.bnk_marketproject_be.entity.AdminInquiry; // ← BOARD를 매핑한 QNA 엔티티
import kr.co.bnk_marketproject_be.repository.AdminInquiryRepository;
import kr.co.bnk_marketproject_be.service.AdminInquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminInquiryServiceImpl implements AdminInquiryService {

    private final AdminInquiryRepository repo;

    @Override
    public List<AdminInquiryDTO> latest(int limit) {
        var page = PageRequest.of(0, limit);

        // CREATED_AT이 문자열이므로 정렬 안전하게 PK 역순 권장
        var list = repo.findByBoardTypeOrderByIdDesc("inquiry",page);

        return list.stream()
                .map(q -> AdminInquiryDTO.builder()
                        .id(q.getId())
                        .title(q.getTitle())
                        .createdAt(q.getCreatedAt())   // 문자열 그대로 전달
                        .qnaStatus("NEW")
                        .userId(q.getUserId())
                        .build())
                .toList();
    }
}