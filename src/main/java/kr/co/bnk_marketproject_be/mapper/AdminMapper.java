package kr.co.bnk_marketproject_be.mapper;

import kr.co.bnk_marketproject_be.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminMapper {

    // @Param : Mybatis SQL 매퍼파일(xml)에서 해당 매개변수를 참조할 수 있는 어노테이션, 반드시 선언
    public List<CouponsDTO> selectAllCoupons(@Param("pageRequestDTO") PageRequestDTO pageRequestDTO);
    public List<CouponsNowDTO> selectAllCouponsNow(@Param("pageRequestDTO") PageRequestDTO pageRequestDTO);
    public List<AdminEmployDTO> selectAllAdminEmploy(@Param("pageRequestDTO") PageRequestDTO pageRequestDTO);
    public List<AdminAnnouncementDTO> selectAllAdminAnnouncement(@Param("pageRequestDTO") PageRequestDTO pageRequestDTO);
    public List<AdminFAQDTO> selectAllAdminFAQ(@Param("pageRequestDTO") PageRequestDTO pageRequestDTO);
    public List<AdminInquiryDTO> selectAllAdminInquiry(@Param("pageRequestDTO") PageRequestDTO pageRequestDTO);

    // 항상 동일(total 세기)
    public int selectCountTotal(@Param("pageRequestDTO") PageRequestDTO pageRequestDTO);
    public int selectCountTotalCouponsNow(@Param("pageRequestDTO") PageRequestDTO pageRequestDTO);
    public int selectCountTotalAdminEmploy(@Param("pageRequestDTO") PageRequestDTO pageRequestDTO);
    public int selectCountTotalAdminAnnouncement(@Param("pageRequestDTO") PageRequestDTO pageRequestDTO);
    public int selectCountTotalAdminFAQ(@Param("pageRequestDTO") PageRequestDTO pageRequestDTO);
    public int selectCountTotalAdminInquiry(@Param("pageRequestDTO") PageRequestDTO pageRequestDTO);

    // 관리자_고객센터_채용하기 선택삭제 구현
    public void deleteAdminEmploy(int id);

    // 관리자_고객센터_문의하기 댓글 구현
    public AdminCommentDTO selectInquiryComment(@Param("AdminCommentDTO") AdminCommentDTO adminCommentDTO);

    // 수정을 위한 불러오기(회원수정)
    public AdminMemberDTO selectMember(@Param("user_id") String user_id);
    // 탈퇴된 회원 정보 수거(아이디 제외 null)
    public void updateMember(@Param("userId") String userId);

    // 상품 삭제
    public void deleteProduct(@Param("product_code") int product_code);

    // 배송상세
    public DeliveriesDTO selectDeliveries(@Param("order_code") String order_code);
}