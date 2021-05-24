package wooteco.subway.auth.application;

public class AuthorizationException extends RuntimeException {
    private static final String MESSAGE = "토근인증에 실패했습니다.";
    public AuthorizationException() {
        super(MESSAGE);
    }
}
