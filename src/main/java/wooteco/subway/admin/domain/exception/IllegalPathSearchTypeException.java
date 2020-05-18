package wooteco.subway.admin.domain.exception;

public class IllegalPathSearchTypeException extends RuntimeException {
    public IllegalPathSearchTypeException() {
        super("잘못된 가중치를 가지고 있습니다.");
    }
}
