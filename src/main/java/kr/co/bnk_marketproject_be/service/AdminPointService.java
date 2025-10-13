package kr.co.bnk_marketproject_be.service;

import com.querydsl.core.Tuple;
import jakarta.transaction.Transactional;
import kr.co.bnk_marketproject_be.dto.AdminPointDTO;
import kr.co.bnk_marketproject_be.dto.PageRequestDTO;
import kr.co.bnk_marketproject_be.dto.PageResponseAdminPointDTO;
import kr.co.bnk_marketproject_be.entity.AdminPoint;
import kr.co.bnk_marketproject_be.repository.AdminPointRepository;
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
public class AdminPointService {

    private final AdminPointRepository adminPointRepository;

    private final ModelMapper modelMapper;

    public PageResponseAdminPointDTO findAdminPointAll(PageRequestDTO pageRequestDTO){

        Pageable pageable = pageRequestDTO.getPageable("id");

        Page<Tuple> pageTuple = null;
        if(pageRequestDTO.getSearchType() != null){
            // 검색 글 검색
            pageTuple = adminPointRepository.selectAdminPointAllForSearch(pageRequestDTO, pageable);
        }else{
            // 일반 글 목록
            pageTuple = adminPointRepository.selectAdminPointAllForList(pageRequestDTO, pageable);
        }

        log.info("pageTuple={}",pageTuple);
        List<Tuple> tupleList = pageTuple.getContent();
        int total = (int)pageTuple.getTotalElements();

        List<AdminPointDTO> dtoList = tupleList.stream()
                .map(tuple -> {
                    AdminPoint adminPoint = tuple.get(0, AdminPoint.class);
                    String name = tuple.get(1, String.class);
                    String userId = tuple.get(2, String.class);
                    String email = tuple.get(3, String.class);
                    String phone = tuple.get(4, String.class);
                    String address = tuple.get(5,String.class);
                    String role = tuple.get(6, String.class);

                    adminPoint.setName(name);
                    adminPoint.setUserId(userId);
                    adminPoint.setEmail(email);
                    adminPoint.setPhone(phone);
                    adminPoint.setAddress(address);
                    adminPoint.setRole(role);


                    return modelMapper.map(adminPoint, AdminPointDTO.class);
                })
                .toList();

        return PageResponseAdminPointDTO.builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();
    }

    @Transactional
    public void deleteStores(List<Integer> ids){
        adminPointRepository.deleteAllById(ids);
    }
}