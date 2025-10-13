package kr.co.bnk_marketproject_be.sms;

public interface SmsSender {

    /**
     * 실제 문자 전송을 수행
     * @param to 받는 사람 전화번호 (예: 01012345678)
     * @param text 보낼 메시지 내용
     * @return 전송 성공 여부
     */
    boolean sendSms(String to, String text);

}
