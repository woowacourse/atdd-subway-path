package wooteco.subway.application.exception;

public class InvalidAgeException extends IllegalArgumentException {

    public InvalidAgeException(int age) {
        super(String.format("입력 나이(%d)는 1살이상이여야 합니다.", age));
    }
}
