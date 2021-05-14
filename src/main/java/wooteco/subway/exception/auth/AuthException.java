package wooteco.subway.exception.auth;

public class AuthException extends RuntimeException {

    public AuthException(AuthExceptionStatus authExceptionStatus) {
        super(authExceptionStatus.getMessage());
    }
}
