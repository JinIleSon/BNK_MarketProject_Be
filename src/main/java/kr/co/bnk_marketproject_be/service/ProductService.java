package kr.co.bnk_marketproject_be.service;

import kr.co.bnk_marketproject_be.dto.ProductViewsDTO;

public interface ProductService {
    ProductViewsDTO getProductDetail(int id);
}

