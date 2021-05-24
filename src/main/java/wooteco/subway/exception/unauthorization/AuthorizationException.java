package wooteco.subway.exception.unauthorization;

public class AuthorizationException extends UnAuthorizationException {
    private static final String MESSAGE = "인증 불가능한 사용자 입니다.";

    public AuthorizationException() {
        super(MESSAGE);
    }

    public AuthorizationException(String message) {
        super(message);
    }
}
