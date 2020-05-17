package wooteco.subway.admin.exception;

public class NotFoundInputValueException extends IllegalArgumentException {
    private static final String ERROR_MESSAGE = "입력값이 없습니다 [%s]";

    public NotFoundInputValueException(String nullObject) {
        super(String.format(ERROR_MESSAGE, nullObject));
    }
}
