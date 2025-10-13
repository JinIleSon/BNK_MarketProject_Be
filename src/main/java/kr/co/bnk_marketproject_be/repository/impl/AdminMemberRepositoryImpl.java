package kr.co.bnk_marketproject_be.repository.impl;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.bnk_marketproject_be.dto.PageRequestDTO;
import kr.co.bnk_marketproject_be.entity.QAdminMember;
import kr.co.bnk_marketproject_be.entity.QUser;
import kr.co.bnk_marketproject_be.repository.custom.AdminMemberRepositoryCustom;
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
public class AdminMemberRepositoryImpl implements AdminMemberRepositoryCustom {

    /**
     * 반드시 파일명을 AdminMemberRepositoryImpl로 해야함.
     * 다른 이름으로 하면 QueryDSL 생성 에러가 발생됨.
     */

    private final JPAQueryFactory jpaQueryFactory;

    private QAdminMember qAdminMember = QAdminMember.adminMember;
    private QUser qUser = QUser.user;


    @Override
    public Page<Tuple> selectAdminMemberAllForList(PageRequestDTO pageRequestDTO, Pageable pageable) {

        List<Tuple> tupleList = jpaQueryFactory.select(qAdminMember, qUser.name, qUser.userId, qUser.email, qUser.phone, qUser.address, qUser.role)
                .from(qAdminMember)
                .join(qUser)
                .on(qAdminMember.rep.eq(qUser.name))
                .where(qAdminMember.boardType.eq("memberlist"))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qAdminMember.id.desc())
                .fetch();

        // 전체 게시물 개수
        long total = jpaQueryFactory
                .select(qAdminMember.count())
                .from(qAdminMember)
                .where(qAdminMember.boardType.eq("memberlist"))
                .fetchOne();

        return new PageImpl<Tuple>(tupleList, pageable, total);
    }

    public Page<Tuple> selectAdminMemberAllForSearch(PageRequestDTO pageRequestDTO, Pageable pageable) {

        String searchType = pageRequestDTO.getSearchType();
        String keyword = pageRequestDTO.getKeyword();

        // 검색 타입에 따라 where 조건 표현식 생성(동적 쿼리)
        BooleanExpression expression = null;

        log.info("searchType:{}",  searchType);
        log.info("keyword:{}",  keyword);
        if(searchType.equals("userId")){
            expression = qUser.userId.contains(keyword);
        }else if(searchType.equals("name")){
            expression = qUser.name.contains(keyword);
        }else if(searchType.equals("email")){
            expression = qUser.email.contains(keyword);
        }else if(searchType.equals("phone")){
            expression = qUser.phone.contains(keyword);
        }
        log.info("expression:{}", expression.toString());
        // 기본 조건: boardType = 'storelist'
        BooleanExpression boardTypeExpr = qAdminMember.boardType.eq("memberlist");

        List<Tuple> tupleList = jpaQueryFactory.select(qAdminMember, qUser.name, qUser.userId, qUser.email, qUser.phone, qUser.address, qUser.role)
                .from(qAdminMember)
                .join(qUser)
                .on(qAdminMember.rep.eq(qUser.name)) // 대표(명)과 사람이름 같으면
                .where(boardTypeExpr.and(expression))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qAdminMember.id.desc())
                .fetch();

        // 전체 게시물 개수
        long total = jpaQueryFactory
                .select(qAdminMember.count())
                .from(qAdminMember)
                .join(qUser)
                .on(qAdminMember.rep.eq(qUser.name))
                .where(boardTypeExpr.and(expression))
                .fetchOne();

        log.info("total:{}", total);
        log.info("tuplelist:{}", tupleList.toString());
        return new PageImpl<Tuple>(tupleList, pageable, total);
    }
}
