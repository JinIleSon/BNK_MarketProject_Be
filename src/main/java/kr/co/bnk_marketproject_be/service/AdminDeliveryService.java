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

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdminDeliveryService {

    private final AdminDeliveryRepository adminDeliveryRepository;

    private final AdminMapper adminMapper;

    private final ModelMapper modelMapper;

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
                    Number total_amount_number = tuple.get(3, Number.class);
                    int total_amount = total_amount_number.intValue();
                    Number itemCountWithDeliveryJoin = tuple.get(4, Number.class);
                    int item_count = itemCountWithDeliveryJoin.intValue();

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

    public DeliveriesDTO selectDelivery(String order_code){
        DeliveriesDTO deliveriesDTO = adminMapper.selectDeliveries(order_code);
        return deliveriesDTO;
    }
}