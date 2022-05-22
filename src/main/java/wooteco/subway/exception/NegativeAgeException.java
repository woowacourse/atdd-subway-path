package wooteco.subway.exception;

public class NegativeAgeException extends RuntimeException {

    public NegativeAgeException() {
        super("나이는 음수일 수 없습니다.");
    }
}
