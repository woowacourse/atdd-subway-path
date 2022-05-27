package wooteco.subway.application.exception;

public class InvalidAgeException extends IllegalArgumentException {

    public InvalidAgeException() {
        super("나이는 음수이거나 150초과일 수는 없습니다.");
    }
}
