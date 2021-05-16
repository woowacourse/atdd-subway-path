package wooteco.subway.exception;

public class NotValidToken extends RuntimeException {

    private static final String MESSAGE = "유효하지 않은 토큰입니다.";

    public NotValidToken() {
        super(MESSAGE);
    }

}
