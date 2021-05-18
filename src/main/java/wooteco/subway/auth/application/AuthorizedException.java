package wooteco.subway.auth.application;

public class AuthorizedException extends RuntimeException {
    public AuthorizedException(final String message) {
        super(message);
    }
}
