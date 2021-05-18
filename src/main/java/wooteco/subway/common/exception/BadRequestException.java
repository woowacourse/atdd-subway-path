package wooteco.subway.common.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends HttpException {
    private static final HttpStatus STATUS_CODE = HttpStatus.BAD_REQUEST;
    private static final String ERROR_MESSAGE = "잘못된 요청입니다.";

    public BadRequestException() {
        this(ERROR_MESSAGE);
    }

    public BadRequestException(String message) {
        super(STATUS_CODE, message);
    }
}
