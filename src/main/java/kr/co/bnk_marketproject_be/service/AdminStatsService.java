package kr.co.bnk_marketproject_be.service;

import kr.co.bnk_marketproject_be.dto.AdminOpsStatsDTO;

import java.time.LocalDateTime;

public interface AdminStatsService {
    AdminOpsStatsDTO getOpsStats(LocalDateTime startAt, LocalDateTime endAt);
}
