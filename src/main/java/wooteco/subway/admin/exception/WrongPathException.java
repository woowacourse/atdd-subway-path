package wooteco.subway.admin.exception;

public class WrongPathException extends RuntimeException {

    public static final String WRONG_PATH_EXCEPTION_MESSAGE = "잘못된 입력입니다.";

    public WrongPathException() {
        super(WRONG_PATH_EXCEPTION_MESSAGE);
    }
}
