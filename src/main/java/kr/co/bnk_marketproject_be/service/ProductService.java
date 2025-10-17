package kr.co.bnk_marketproject_be.service;

import kr.co.bnk_marketproject_be.dto.PageRequestProductDTO;
import kr.co.bnk_marketproject_be.dto.PageResponseProductDTO;
import kr.co.bnk_marketproject_be.dto.ProductBoardsDTO;
import kr.co.bnk_marketproject_be.dto.ProductViewsDTO;

public interface ProductService {
    ProductViewsDTO getProductDetail(int id);
    PageResponseProductDTO<ProductBoardsDTO> getProductReviewPage(int productId, PageRequestProductDTO req);
}

