package wooteco.subway.auth.exception;

public class JwtNotAuthorizationException extends AuthException {
    private static final String MESSAGE = "유효하지 않은 토큰임!";

    public JwtNotAuthorizationException() {
        super(MESSAGE);
    }
}
