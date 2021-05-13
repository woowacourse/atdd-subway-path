package wooteco.subway.member.exception;

public class InvalidTokenException extends RuntimeException {
    private static final String MESSAGE = "토큰이 유효하지 않습니다.";

    public InvalidTokenException() {
        super(MESSAGE);
    }

}
