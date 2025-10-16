package kr.co.bnk_marketproject_be.mapper;

import kr.co.bnk_marketproject_be.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MyPageMapper {

    // 푸시용 주석
    // 마이페이지/나의설정 - 손진일 추가
    public UserDTO selectUser(@Param("user_id") String user_id);

    // 마이페이지/포인트내역 - 손진일 추가
    public List<AdminPointDTO> selectUserPoint(@Param("pageRequestDTO") PageRequestDTO pageRequestDTO, @Param("user_id") String userId);
    public int selectCountTotalUserPoint(@Param("pageRequestDTO") PageRequestDTO pageRequestDTO, @Param("user_id") String userId);

    // 마이페이지/쿠폰 - 손진일 추가
    public List<MyPageCouponsNowDTO> selectUserCouponsNow(@Param("pageRequestDTO") PageRequestDTO pageRequestDTO, @Param("user_id") String userId);
    public int selectCountTotalUserCouponsNow(@Param("pageRequestDTO") PageRequestDTO pageRequestDTO, @Param("user_id") String userId);

    // 마이페이지/나의리뷰 - 손진일 추가
    public List<MyPageReviewDTO> selectUserReview(@Param("pageRequestDTO") PageRequestDTO pageRequestDTO, @Param("user_id") String userId);
    public int selectCountTotalUserReview(@Param("pageRequestDTO") PageRequestDTO pageRequestDTO, @Param("user_id") String userId);

    // 마이페이지/문의하기 - 손진일 추가
    public List<AdminInquiryDTO> selectAllInquiry(@Param("pageRequestDTO") PageRequestDTO pageRequestDTO, @Param("userId") String userId);
    public int selectCountTotalInquiry(@Param("pageRequestDTO") PageRequestDTO pageRequestDTO, @Param("userId") String userId);


}
