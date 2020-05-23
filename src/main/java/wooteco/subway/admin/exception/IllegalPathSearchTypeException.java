package wooteco.subway.admin.exception;

public class IllegalPathSearchTypeException extends RuntimeException {
    private static final String MESSAGE = "유효한 경로 탐색 타입이 아닙니다.";

    public IllegalPathSearchTypeException() {
        super(MESSAGE);
    }
}
