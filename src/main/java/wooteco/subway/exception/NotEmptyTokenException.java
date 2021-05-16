package wooteco.subway.exception;

public class NotEmptyTokenException extends RuntimeException {

    private static final String MESSAGE = "토큰이 존재하지 않습니다.";

    public NotEmptyTokenException() {
        super(MESSAGE);
    }

}
