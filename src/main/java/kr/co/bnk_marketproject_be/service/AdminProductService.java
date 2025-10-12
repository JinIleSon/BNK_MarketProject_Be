package kr.co.bnk_marketproject_be.service;

import com.querydsl.core.Tuple;
import jakarta.transaction.Transactional;
import kr.co.bnk_marketproject_be.dto.ProductsDTO;
import kr.co.bnk_marketproject_be.dto.PageRequestDTO;
import kr.co.bnk_marketproject_be.dto.PageResponseAdminProductDTO;
import kr.co.bnk_marketproject_be.dto.ProductsDTO;
import kr.co.bnk_marketproject_be.entity.Products;
import kr.co.bnk_marketproject_be.entity.Products;
import kr.co.bnk_marketproject_be.repository.AdminProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdminProductService {

    private final AdminProductRepository adminProductRepository;

    private final ModelMapper modelMapper;

    public PageResponseAdminProductDTO findAdminProductAll(PageRequestDTO pageRequestDTO){

        Pageable pageable = pageRequestDTO.getPageable("id");

        Page<Tuple> pageTuple = null;
        if(pageRequestDTO.getSearchType() != null){
            // 검색 글 검색
            pageTuple = adminProductRepository.selectAdminProductAllForSearch(pageRequestDTO, pageable);
        }else{
            // 일반 글 목록
            pageTuple = adminProductRepository.selectAdminProductAllForList(pageRequestDTO, pageable);
        }

        log.info("pageTuple={}",pageTuple);
        List<Tuple> tupleList = pageTuple.getContent();
        int total = (int)pageTuple.getTotalElements();

        List<ProductsDTO> dtoList = tupleList.stream()
                .map(tuple -> {
                    String url = tuple.get(0, String.class);
                    Products products = tuple.get(1, Products.class);
                    String user_name = tuple.get(2, String.class);

                    products.setUrl(url);
                    products.setUser_name(user_name);

                    log.info("url={}",url);
                    log.info("user_name={}", user_name);
                    return modelMapper.map(products, ProductsDTO.class);
                })
                .toList();

        return PageResponseAdminProductDTO.builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();
    }

    @Transactional
    public void deleteProducts(List<Integer> ids) {
        log.info("ids:{}",ids);
        adminProductRepository.deleteAllById(ids);
    }
}