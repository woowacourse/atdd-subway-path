package wooteco.subway.exception.auth;

public class InvalidMemberException extends AuthException {
    public InvalidMemberException(String email) {
        super(email + "에 해당하는 회원이 없습니다.");
    }
}
