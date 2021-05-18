package wooteco.subway.exception;

public class EmptyTokenException extends AuthorizationException {
    private static final String MESSAGE = "토큰이 존재하지 않습니다.";

    public EmptyTokenException() {
        super(MESSAGE);
    }
}
