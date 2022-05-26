package wooteco.subway.exception;

import org.springframework.http.HttpStatus;

public class NotEmptyFieldException extends SubwayException {

    private static final String MESSAGE = "필드의 값은 존재해야합니다.";

    public NotEmptyFieldException() {
        super(MESSAGE, HttpStatus.BAD_REQUEST);
    }

    public NotEmptyFieldException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
