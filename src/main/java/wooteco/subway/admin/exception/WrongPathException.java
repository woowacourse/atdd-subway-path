package wooteco.subway.admin.exception;

public class WrongPathException extends RuntimeException {

    public static final String WRONG_PATH_EXCEPTION_MESSAGE = "WRONG_PATH_PARAMS";

    public WrongPathException() {
        super(WRONG_PATH_EXCEPTION_MESSAGE);
    }
}
