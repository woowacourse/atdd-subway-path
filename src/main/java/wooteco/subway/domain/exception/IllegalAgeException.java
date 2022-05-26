package wooteco.subway.domain.exception;

public class IllegalAgeException extends ExpectedException {

    private static final String MESSAGE = "정상적인 나이가 아닙니다.";

    public IllegalAgeException() {
        super(MESSAGE);
    }
}
