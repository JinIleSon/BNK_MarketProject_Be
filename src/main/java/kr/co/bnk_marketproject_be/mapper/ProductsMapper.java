package kr.co.bnk_marketproject_be.mapper;

import kr.co.bnk_marketproject_be.dto.PageRequestProductDTO;
import kr.co.bnk_marketproject_be.dto.ProductsDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductsMapper {
    List<ProductsDTO> selectProductListPaged(@Param("page") PageRequestProductDTO page,
                                             @Param("sort") String sort);
    int selectTotalProductCount();

    List<ProductsDTO> selectProductSearch(@Param("req") PageRequestProductDTO req);
    int selectProductSearchTotal(@Param("req") PageRequestProductDTO req);

}
