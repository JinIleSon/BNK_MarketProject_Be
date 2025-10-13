package kr.co.bnk_marketproject_be.service;

import kr.co.bnk_marketproject_be.dto.AdminNoticeDTO;

import java.util.List;

public interface AdminNoticeService {
    List<AdminNoticeDTO> getLatest(int limit);
}

