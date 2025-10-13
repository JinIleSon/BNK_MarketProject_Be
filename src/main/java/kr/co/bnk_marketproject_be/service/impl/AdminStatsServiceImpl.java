package kr.co.bnk_marketproject_be.service.impl;

import kr.co.bnk_marketproject_be.dto.AdminOpsStatsDTO;
import kr.co.bnk_marketproject_be.repository.AdminStatsRepository;
import kr.co.bnk_marketproject_be.repository.projection.CategorySalesRow;
import kr.co.bnk_marketproject_be.repository.projection.DailyOpsRow;
import kr.co.bnk_marketproject_be.service.AdminStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AdminStatsServiceImpl implements AdminStatsService {

    private final AdminStatsRepository repo;

    @Override
    public AdminOpsStatsDTO getOpsStats(LocalDateTime startAt, LocalDateTime endAt) {

        // ====== 1) 일자별 막대차트 ======
        var dailyRows = repo.aggregateDailyOps(startAt, endAt);
        Map<LocalDate, AdminOpsStatsDTO.DailyOpsDTO> dailyMap = new HashMap<>();

        for (Object[] row : dailyRows) {
            String ymd = (String) row[0];
            long orderCnt = ((Number) row[1]).longValue();
            long payCnt   = ((Number) row[2]).longValue();
            long cancelCnt= ((Number) row[3]).longValue();

            LocalDate date = LocalDate.parse(ymd); // "YYYY-MM-DD" 포맷 가정
            dailyMap.put(date,
                    new AdminOpsStatsDTO.DailyOpsDTO(date, orderCnt, payCnt, cancelCnt));
        }

        var days = dateRange(startAt.toLocalDate(), endAt.toLocalDate().minusDays(1));
        var daily = days.stream()
                .map(d -> dailyMap.getOrDefault(d,
                        AdminOpsStatsDTO.DailyOpsDTO.builder().date(d).orders(0).payments(0).cancels(0).build()))
                .toList();

        // ====== 2) 카테고리별 매출 파이 ======
        var catRows = repo.aggregateCategorySales(startAt, endAt);

        var categorySales = catRows.stream()
                .map(r -> {
                    Object[] row = (Object[]) r;
                    String category = (String) row[0];
                    Long amount = ((Number) row[1]).longValue();

                    return AdminOpsStatsDTO.CategorySalesDTO.builder()
                            .category(category == null || category.isBlank() ? "기타" : category)
                            .amount(amount)
                            .build();
                })
                .toList();

        long totalSales = categorySales.stream()
                .mapToLong(AdminOpsStatsDTO.CategorySalesDTO::getAmount)
                .sum();


        // ====== 3) 카드형 지표 (너의 기존 메서드 활용) ======
        long pending = repo.countPendingPayment(startAt, endAt);
        long ready   = repo.countShippingReady(startAt, endAt);
        long cancelR = repo.countCancelRequest(startAt, endAt);
        long exchR   = repo.countExchangeRequest(startAt, endAt);
        long retR    = repo.countReturnRequest(startAt, endAt);
        long orders  = repo.countOrders(startAt, endAt);
        long amount  = repo.sumOrderAmount(startAt, endAt);
        long signups = repo.countUserSignups(startAt, endAt);
        long qnas    = repo.countQna(startAt, endAt);

        return AdminOpsStatsDTO.builder()
                .dailyStats(daily)
                .categorySales(categorySales)
                .totalSales(totalSales)
                .pendingPayment(pending)
                .shippingReady(ready)
                .cancelRequest(cancelR)
                .exchangeRequest(exchR)
                .returnRequest(retR)
                .orderCount(orders)
                .orderAmount(amount)
                .userSignups(signups)
                .visitors(0) // 추후 로그 기반 집계로 교체
                .qnaCount(qnas)
                .build();
    }

    private static List<LocalDate> dateRange(LocalDate from, LocalDate toInclusive) {
        if (from.isAfter(toInclusive)) return List.of();
        long days = toInclusive.toEpochDay() - from.toEpochDay();
        return Stream.iterate(from, d -> d.plusDays(1)).limit(days + 1).toList();
    }
}
