package wooteco.subway.admin.exception;

public class NotFoundLineException extends RuntimeException {
    public static final String NO_EXIST_LINE = "존재하지 않는 노선입니다.";

    public NotFoundLineException() {
        super(NO_EXIST_LINE);
    }
}
