package wooteco.subway.auth.application;

public class AuthorizedException extends RuntimeException {
    public AuthorizedException() {
        super("적절하지 않은 권한입니다.");
    }

    public AuthorizedException(final String message) {
        super(message);
    }
}
