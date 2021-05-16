package wooteco.subway.exception;

public class AuthException extends RuntimeException {

    public AuthException(AuthExceptionStatus authExceptionStatus) {
        super(authExceptionStatus.getMessage());
    }
}
