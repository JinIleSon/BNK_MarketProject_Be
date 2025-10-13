package kr.co.bnk_marketproject_be.repository.custom;


import com.querydsl.core.Tuple;
import kr.co.bnk_marketproject_be.dto.PageRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminDeliveryRepositoryCustom {

    public Page<Tuple> selectAdminDeliveryAllForList(PageRequestDTO pageRequestDTO, Pageable pageable);
    public Page<Tuple> selectAdminDeliveryAllForSearch(PageRequestDTO pageRequestDTO, Pageable pageable);

}



