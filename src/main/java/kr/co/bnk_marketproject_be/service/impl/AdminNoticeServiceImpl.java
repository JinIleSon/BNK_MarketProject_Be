package kr.co.bnk_marketproject_be.service.impl;

import kr.co.bnk_marketproject_be.dto.AdminNoticeDTO;
import kr.co.bnk_marketproject_be.entity.AdminNotice;
import kr.co.bnk_marketproject_be.repository.AdminNoticeRepository;
import kr.co.bnk_marketproject_be.service.AdminNoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminNoticeServiceImpl implements AdminNoticeService {

    private final AdminNoticeRepository repo;

    @Override
    public List<AdminNoticeDTO> getLatest(int limit) {
        var page = PageRequest.of(0, limit);

        // ✅ boardType 인자 제거
        var list = repo.findAllByOrderByIdDesc(page);

        return list.stream()
                .map(n -> AdminNoticeDTO.builder()
                        .id(n.getId())
                        .title(n.getTitle())
                        .createdAt(n.getCreatedAt())   // 현재는 문자열 그대로
                        .userId(n.getUserId())
                        .pinned(false)                 // 템플릿에서 참조하므로 기본값 세팅
                        .build())
                .toList();
    }
}
