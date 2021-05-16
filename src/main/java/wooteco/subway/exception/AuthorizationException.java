package wooteco.subway.exception;

public class AuthorizationException extends RuntimeException {
    private static final String MESSAGE = "인증에 실패하였습니다.";

    public AuthorizationException() {
        super(MESSAGE);
    }
}
