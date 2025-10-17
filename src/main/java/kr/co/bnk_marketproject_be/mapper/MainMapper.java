package kr.co.bnk_marketproject_be.mapper;

import kr.co.bnk_marketproject_be.dto.ProductCardDTO;
import kr.co.bnk_marketproject_be.entity.Products;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MainMapper {

    List<ProductCardDTO> selectProductOrderBy(@Param("sort") String sort,
                                              @Param("limit") int limit);
}
