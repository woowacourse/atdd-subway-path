package wooteco.subway.exception;

public class AuthorizationException extends RuntimeException {

    private static final String MESSAGE = "인증 정보가 유효하지 않습니다.";

    public AuthorizationException() {
        super(MESSAGE);
    }
}
