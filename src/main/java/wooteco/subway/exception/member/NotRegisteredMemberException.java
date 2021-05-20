package wooteco.subway.exception.member;

public class NotRegisteredMemberException extends RuntimeException {

    public NotRegisteredMemberException() {
        super("[ERROR] 등록되지 않은 회원입니다.");
    }
}
