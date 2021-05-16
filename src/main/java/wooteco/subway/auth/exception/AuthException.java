package wooteco.subway.auth.exception;

public class AuthException extends RuntimeException {

    public AuthException(AuthExceptionStatus authExceptionStatus) {
        super(authExceptionStatus.getMessage());
    }
}
