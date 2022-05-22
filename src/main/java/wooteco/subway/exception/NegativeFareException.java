package wooteco.subway.exception;

public class NegativeFareException extends RuntimeException {

    public NegativeFareException() {
        super("금액은 음수일 수 없습니다.");
    }
}
