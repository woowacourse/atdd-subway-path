package wooteco.subway.admin.exception;

public class NotFoundLineException extends RuntimeException {
    public NotFoundLineException() {
        super("Line을 찾을 수 없습니다.");
    }
}
