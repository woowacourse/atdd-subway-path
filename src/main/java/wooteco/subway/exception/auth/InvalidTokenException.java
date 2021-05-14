package wooteco.subway.exception.auth;

public class InvalidTokenException extends AuthException {
    public InvalidTokenException() {
        super("유효하지 않은 토큰입니다.");
    }
}
