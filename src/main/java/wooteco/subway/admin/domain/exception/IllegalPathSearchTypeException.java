package wooteco.subway.admin.domain.exception;

public class IllegalPathSearchTypeException extends RuntimeException {
    private static final String MESSAGE = "잘못된 가중치를 가지고 있습니다.";

    public IllegalPathSearchTypeException() {
        super(MESSAGE);
    }
}
