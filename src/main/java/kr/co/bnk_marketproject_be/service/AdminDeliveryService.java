package kr.co.bnk_marketproject_be.service;

import com.querydsl.core.Tuple;
import kr.co.bnk_marketproject_be.dto.DeliveriesDTO;
import kr.co.bnk_marketproject_be.dto.DeliveriesDTO;
import kr.co.bnk_marketproject_be.dto.PageRequestDTO;
import kr.co.bnk_marketproject_be.dto.PageResponseAdminDeliveryDTO;
import kr.co.bnk_marketproject_be.entity.Deliveries;
import kr.co.bnk_marketproject_be.entity.Deliveries;
import kr.co.bnk_marketproject_be.mapper.AdminMapper;
import kr.co.bnk_marketproject_be.repository.AdminDeliveryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdminDeliveryService {

    private final AdminDeliveryRepository adminDeliveryRepository;

    private final AdminMapper adminMapper;

    private final ModelMapper modelMapper;

    private static BigDecimal toBigDecimal(Object v) {
        if (v == null) return null;
        if (v instanceof BigDecimal bd) return bd;
        if (v instanceof java.math.BigInteger bi) return new BigDecimal(bi);
        if (v instanceof Number n) return new BigDecimal(n.toString()); // 정밀도 보존
        if (v instanceof String s) return new BigDecimal(s);
        throw new IllegalArgumentException("숫자 아님: " + v.getClass());
    }

    public PageResponseAdminDeliveryDTO findAdminDeliveryAll(PageRequestDTO pageRequestDTO){

        Pageable pageable = pageRequestDTO.getPageable("id");

        Page<Tuple> pageTuple = null;
        if(pageRequestDTO.getSearchType() != null){
            // 검색 글 검색
            pageTuple = adminDeliveryRepository.selectAdminDeliveryAllForSearch(pageRequestDTO, pageable);
        }else{
            // 일반 글 목록
            pageTuple = adminDeliveryRepository.selectAdminDeliveryAllForList(pageRequestDTO, pageable);
        }

        log.info("pageTuple={}",pageTuple);
        List<Tuple> tupleList = pageTuple.getContent();
        int total = (int)pageTuple.getTotalElements();

        List<DeliveriesDTO> dtoList = tupleList.stream()
                .map(tuple -> {


                    Deliveries deliveries = tuple.get(0,  Deliveries.class);
                    String order_code = tuple.get(1,  String.class);
                    String product_name = tuple.get(2,  String.class);
                    // 안전하게 받기
                    Object totalObj         = tuple.get(3, Object.class);                 // 무엇이 오든 받는다
                    BigDecimal totalBd      = toBigDecimal(totalObj);       // 우리 유틸로 안전 변환

                    Number itemCountNum     = tuple.get(4, Number.class);   // Long/Integer 모두 커버
                    int item_count          = (itemCountNum != null) ? itemCountNum.intValue() : 0;

                    int total_amount        = (totalBd != null) ? totalBd.intValue() : 0;

                    deliveries.setOrder_code(order_code);
                    deliveries.setProduct_name(product_name);
                    deliveries.setTotal_amount(total_amount);
                    deliveries.setItem_count(item_count);

                    log.info("deliveries={}",deliveries);
                    log.info("order_code={}",deliveries.getOrder_code());
                    log.info("product_name={}",deliveries.getProduct_name());
                    log.info("total_amount={}",deliveries.getTotal_amount());
                    log.info("item_count={}",deliveries.getItem_count());

                    return modelMapper.map(deliveries, DeliveriesDTO.class);
                })
                .toList();

        return PageResponseAdminDeliveryDTO.builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();
    }

    public void insertDeliveries(DeliveriesDTO deliveriesDTO) {
        adminMapper.insertDeliveries(deliveriesDTO);
    }

    public DeliveriesDTO selectDelivery(String order_code){
        DeliveriesDTO deliveriesDTO = adminMapper.selectDeliveries(order_code);
        return deliveriesDTO;
    }
}