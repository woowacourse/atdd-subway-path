package wooteco.subway.auth.exception;

public class AuthorizationException extends RuntimeException {

    public AuthorizationException(String message) {
        super(String.format("인증에 실패했습니다. (%s)", message));
    }
}
