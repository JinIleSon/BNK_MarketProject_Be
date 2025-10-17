package kr.co.bnk_marketproject_be.service;

import kr.co.bnk_marketproject_be.dto.ProductCardDTO;
import kr.co.bnk_marketproject_be.entity.Products;
import kr.co.bnk_marketproject_be.mapper.MainMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MainService {

    private final MainMapper mainMapper;

    public List<ProductCardDTO> getProducts(String sort, int limit) {
        return mainMapper.selectProductOrderBy(sort, limit);
    }
}
