package kr.co.bnk_marketproject_be.repository.impl;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.bnk_marketproject_be.dto.PageRequestDTO;
import kr.co.bnk_marketproject_be.entity.QProducts;
import kr.co.bnk_marketproject_be.entity.QProductImages;
import kr.co.bnk_marketproject_be.entity.QProducts;
import kr.co.bnk_marketproject_be.entity.QSeller;
import kr.co.bnk_marketproject_be.entity.QUser;
import kr.co.bnk_marketproject_be.repository.custom.AdminProductRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Repository
public class AdminProductRepositoryImpl implements AdminProductRepositoryCustom {

    /**
     * 반드시 파일명을 AdminProductRepositoryImpl로 해야함.
     * 다른 이름으로 하면 QueryDSL 생성 에러가 발생됨.
     */

    private final JPAQueryFactory jpaQueryFactory;

    private QProducts qProducts = QProducts.products;
    private QUser qUser = QUser.user;
    private QSeller qSeller = QSeller.seller;
    private QProductImages qProductImages = QProductImages.productImages;


    @Override
    public Page<Tuple> selectAdminProductAllForList(PageRequestDTO pageRequestDTO, Pageable pageable) {

        List<Tuple> tupleList = jpaQueryFactory.select(qProductImages.url, qProducts, qUser.name)
                .from(qProducts)
                .leftJoin(qProductImages)
                .on(qProducts.id.eq(qProductImages.products_id))
                .leftJoin(qSeller)
                .on(qProducts.sellers_id.eq(qSeller.id))
                .leftJoin(qUser)
                .on(qSeller.sellerId.eq(qUser.userId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qProducts.id.desc())
                .fetch();

        // 전체 게시물 개수
        long total = jpaQueryFactory
                .select(qProducts.count())
                .from(qProducts)
                .fetchOne();

        return new PageImpl<Tuple>(tupleList, pageable, total);
    }

    public Page<Tuple> selectAdminProductAllForSearch(PageRequestDTO pageRequestDTO, Pageable pageable) {

        String searchType = pageRequestDTO.getSearchType();
        String keyword = pageRequestDTO.getKeyword();

        // 검색 타입에 따라 where 조건 표현식 생성(동적 쿼리)
        BooleanExpression expression = null;

        log.info("searchType:{}", searchType);
        log.info("keyword:{}", keyword);
        if (searchType.equals("product_name")) {
            expression = qProducts.product_name.contains(keyword);
        } else if (searchType.equals("product_code")) {
            expression = qProducts.product_code.stringValue().contains(keyword);
        } else if (searchType.equals("name")) {
            expression = qUser.name.contains(keyword);
            log.info("expression:{}", expression.toString());
        }
        List<Tuple> tupleList = jpaQueryFactory.select(qProductImages.url, qProducts, qUser.name)
                .from(qProducts)
                .leftJoin(qProductImages)
                .on(qProducts.id.eq(qProductImages.products_id))
                .leftJoin(qSeller)
                .on(qProducts.sellers_id.eq(qSeller.id))
                .leftJoin(qUser)
                .on(qSeller.sellerId.eq(qUser.userId))
                .where(expression)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qProducts.id.desc())
                .fetch();

        // 전체 게시물 개수
        long total = jpaQueryFactory
                .select(qProducts.count())
                .from(qProducts)
                .leftJoin(qProductImages)
                .on(qProducts.id.eq(qProductImages.products_id))
                .leftJoin(qSeller)
                .on(qProducts.sellers_id.eq(qSeller.id))
                .leftJoin(qUser)
                .on(qSeller.sellerId.eq(qUser.userId))
                .where(expression)
                .fetchOne();

        log.info("total:{}", total);
        log.info("tuplelist:{}", tupleList.toString());
        return new PageImpl<Tuple>(tupleList, pageable, total);

    }
}