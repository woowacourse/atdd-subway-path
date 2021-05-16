package wooteco.subway.exception.auth;

import org.springframework.http.HttpStatus;

public class InvalidMemberException extends AuthException {
    public InvalidMemberException(String email) {
        super(HttpStatus.UNAUTHORIZED, email + "에 해당하는 회원이 없습니다.");
    }
}
