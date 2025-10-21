package kr.co.bnk_marketproject_be.controller;

import kr.co.bnk_marketproject_be.dto.*;
import kr.co.bnk_marketproject_be.service.MyPageService;
import kr.co.bnk_marketproject_be.service.MypageInquiryService;
import kr.co.bnk_marketproject_be.service.MypageReturnExchangeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import kr.co.bnk_marketproject_be.service.MypageAllOrderService;//주문내역 서비스
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MyPageController {

    // 푸시용 주석
    private final MyPageService myPageService;
    private final MypageAllOrderService mypageAllOrderService; // 추가
    private final MypageInquiryService mypageInquiryService;
    private final MypageAllOrderService orderService;

    @GetMapping("/mypage/mypage/main")
    public String mainpage(Model model, Principal principal, PageRequestDTO pageRequestDTO) {

        if (principal == null) {
            log.warn("⚠️ 비로그인 상태로 마이페이지 접근");
            return "redirect:/login"; // 로그인 안 되어 있으면 로그인 페이지로 이동
        }

        String userId = principal.getName();
        log.info("✅ 로그인 사용자: {}", userId);

        // (추가) 숫자형 userId 구하기
        int dbUserId = myPageService.selectUser(userId).getId();

        // (추가) 모델에 넣어주기
        model.addAttribute("userId", dbUserId);

        // 최근 주문내역 조회
        List<OrdersDTO> recentOrders = mypageAllOrderService.findRecentOrdersByUserId(userId);
        model.addAttribute("recentOrders", recentOrders);
        log.info("최근 주문내역 size = {}", recentOrders.size());
        log.info("최근 주문내역 size 로그 진입");
        if (recentOrders == null) {
            log.warn("⚠️ recentOrders == NULL (Mapper에서 null 리턴됨)");
        } else {
            log.info("🟡 findRecentOrdersByUserId() 결과: {}", recentOrders);
            log.info("최근 주문내역 size = {}", recentOrders.size());
        }


        // ✅ 판매자 정보 디버깅 로그
        for (OrdersDTO order : recentOrders) {
            log.info("🧾 [주문] 코드={}, 판매자명={}, 전화={}, 이메일={}, 사업자번호={}",
                    order.getOrder_code(),
                    order.getSeller_rep(),
                    order.getSeller_tel(),
                    order.getSeller_email(),
                    order.getSeller_bizno());
        }

        // 문의내역 출력_마이페이지_메인#9 포인트적립내역 메인
        PageResponseUserPointDTO pageResponseUserPointDTO = myPageService.selectUserPoint(pageRequestDTO, userId);

        log.info("pageResponseUserPointDTO={}", pageResponseUserPointDTO);
        model.addAttribute("pageResponseUserPointDTO", pageResponseUserPointDTO);

        // 상품평 메인
        PageResponseUserReviewDTO pageResponseReviewDTO = myPageService.selectUserReview(pageRequestDTO, userId);

        log.info("pageResponseReviewDTO={}", pageResponseReviewDTO);
        model.addAttribute("pageResponseReviewDTO", pageResponseReviewDTO);

        // 문의내역 출력_마이페이지_메인#10 문의하기 메인
        PageResponseAdminInquiryDTO pageResponseInquiryDTO = myPageService.selectAllInquiry(pageRequestDTO, userId);

        log.info("pageResponseInquiryDTO={}", pageResponseInquiryDTO);
        model.addAttribute("pageResponseInquiryDTO", pageResponseInquiryDTO);

        // 나의설정 메인
        UserDTO userDTO = myPageService.selectUser(userId);

        log.info("userId = {}", userId);
        userDTO.setUserId(userId);
        log.info("userDTO = {}", userDTO);

        model.addAttribute("userDTO", userDTO);

        return "mypage/mypage_main";
    }
    @GetMapping("/mypage/mypage/point")
    public String pointList(Model model,Principal principal, PageRequestDTO pageRequestDTO,
                            @RequestParam(value = "s", required = false) String s,
                            @RequestParam(value = "e", required = false) String e){
        String userId = principal.getName();

        // 기본값: 최근 7일
        LocalDate today = LocalDate.now();
        LocalDate oneYearAgo = today.minusDays(364);

        LocalDate start = (s != null && !s.isBlank()) ? LocalDate.parse(s) : oneYearAgo;
        LocalDate end   = (e != null && !e.isBlank()) ? LocalDate.parse(e) : today;

        // 보정: start > end 방지 및 365일 제한 (선택사항)
        if (start.isAfter(end)) start = end;
        if (start.isBefore(end.minusDays(364))) start = end.minusDays(364);

        pageRequestDTO.setStartDate(start);
        pageRequestDTO.setEndDate(end);
        pageRequestDTO.setEndExclusive(end.plusDays(1));

        PageResponseUserPointDTO pageResponseUserPointDTO = myPageService.selectUserPoint(pageRequestDTO, userId);

        log.info("pageResponseUserPointDTO={}", pageResponseUserPointDTO);
        // 인풋 기본값 유지를 위해 넘김
        model.addAttribute("s", start.toString());
        model.addAttribute("e", end.toString());
        model.addAttribute("pageResponseDTO", pageResponseUserPointDTO);
        return "mypage/mypage_point";
    }
    @GetMapping("/mypage/mypage/coupon")
    public String couponList(Model model,Principal principal, PageRequestDTO pageRequestDTO){
        String userId = principal.getName();
        PageResponseUserCouponsNowDTO pageResponseUserCouponsNowDTO = myPageService.selectUserCouponsNow(pageRequestDTO, userId);

        log.info("pageResponseUserCouponsNowDTO={}", pageResponseUserCouponsNowDTO);
        model.addAttribute("pageResponseDTO", pageResponseUserCouponsNowDTO);
        return "mypage/mypage_coupon";
    }
    @GetMapping("/mypage/mypage/review")
    public String reviewList(Model model, PageRequestDTO pageRequestDTO, Principal principal){

        String userId = principal.getName();
        PageResponseUserReviewDTO pageResponseReviewDTO = myPageService.selectUserReview(pageRequestDTO, userId);

        log.info("pageResponseReviewDTO={}", pageResponseReviewDTO);
        model.addAttribute("pageResponseDTO", pageResponseReviewDTO);

        return "mypage/mypage_review";
    }
    @GetMapping("/mypage/mypage/ask")
    public String askList(Model model, PageRequestDTO pageRequestDTO, Principal principal){
        String userId = principal.getName();
        PageResponseAdminInquiryDTO pageResponseInquiryDTO = myPageService.selectAllInquiry(pageRequestDTO, userId);

        log.info("pageResponseInquiryDTO={}", pageResponseInquiryDTO);
        model.addAttribute("pageResponseDTO", pageResponseInquiryDTO);

        return "mypage/mypage_ask";
    }
    // 푸시용
    @GetMapping("/mypage/mypage/setup")
    public String setupList(Model  model, Principal principal){

        String userId = principal.getName();
        UserDTO userDTO = myPageService.selectUser(userId);

        log.info("userId = {}", userId);
        userDTO.setUserId(userId);
        log.info("userDTO = {}", userDTO);

        model.addAttribute("userDTO", userDTO);
        return "mypage/mypage_setUp";
    }
    @GetMapping("/mypage/order/detail")
    @ResponseBody
    public List<OrdersDTO> getOrderDetail(@RequestParam String orderCode) {
        System.out.println("📩 주문상세 요청 들어옴: " + orderCode);
        return mypageAllOrderService.getOrderDetailByCode(orderCode);
    }

    @PostMapping("/api/mypage/inquiry")
    @ResponseBody
    public ResponseEntity<String> createInquiry(@RequestBody MypageInquiryDTO dto, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body("로그인이 필요합니다.");
        }

        String userId = principal.getName();
        dto.setStatus("대기중");

        // 로그인 유저 정보 세팅
        int dbUserId = myPageService.selectUser(userId).getId();
        dto.setUserId((long) dbUserId);

        log.info("📩 문의 등록 요청: {}", dto);

        try {
            mypageInquiryService.createInquiry(dto);
            return ResponseEntity.ok("문의가 등록되었습니다.");
        } catch (Exception e) {
            log.error("❌ 문의 등록 실패: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("문의 등록 중 오류 발생");
        }
    }


    @PostMapping("/mypage/mypage/setup")
    public String modify(UserDTO userDTO, Model  model, Principal principal, @RequestParam(required = false) Integer code){

        String userId = principal.getName();
        userDTO.setUserId(userId);

        model.addAttribute("userDTO", userDTO);
        userDTO.setEmail(userDTO.getFirstEmail() + '@' + userDTO.getSecondEmail());
        userDTO.setPhone(userDTO.getFirstPhone() + '-' + userDTO.getSecondPhone() + '-' + userDTO.getThirdPhone());
        model.addAttribute("code", code);

        log.info("userDTO = {}", userDTO);
        log.info("code = {}", code);

        return "/mypage/mypage_passwd";
    }

    @GetMapping("/mypage/mypage/passwd")
    public String passwdList(Principal principal, @RequestParam(required = false) Integer code, Model  model){

        String userId = principal.getName();
        UserDTO userDTO = myPageService.selectUser(userId);

        log.info("userId = {}", userId);
        log.info("userDTO = {}", userDTO);
        userDTO.setUserId(userId);
        model.addAttribute("userDTO", userDTO);
        model.addAttribute("code", code);

        return "mypage/mypage_passwd";
    }

    @GetMapping("/mypage/mypage/passwd-check")
    @ResponseBody
    public boolean checkPassword(@RequestParam("password") String password,
                                 Principal principal) {
        String userId = principal.getName();
        return myPageService.verifyPassword(userId, password);
    }

    @PostMapping("/mypage/mypage/passwd")
    public String codePasswd(Principal principal, @RequestParam(required = false) Integer code, UserDTO userDTO, Model model){

        String userId = principal.getName();

        log.info("code = {}", code);
        log.info("userId = {}", userId);
        log.info("userDTO = {}", userDTO);
        userDTO.setUserId(userId);
        model.addAttribute("userDTO", userDTO);
        model.addAttribute("code", code);

        if(Integer.valueOf(1).equals(code)){
            return "member/member_change_password";
        }
        else if(Integer.valueOf(2).equals(code)){
            myPageService.withdrawUser(userId);
            // 로그아웃/세션 무효화는 시큐리티 필터에서 처리하거나 별도 엔드포인트로
            return "redirect:/main/main/page";
        }
        else if(Integer.valueOf(3).equals(code)){
            // 3) 연락처/주소 정보 업데이트
            myPageService.updateContact(userId, userDTO);
            return "redirect:/mypage/mypage/setup";
        }

        // fallback
        return "redirect:/mypage/mypage/setup";
    }

}
