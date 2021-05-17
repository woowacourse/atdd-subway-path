package wooteco.subway.member.exception;

import org.springframework.http.HttpStatus;
import wooteco.subway.member.exception.message.ErrorMessage;

public class HttpException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final ErrorMessage message;

    public HttpException(HttpStatus httpStatus, ErrorMessage message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public ErrorMessage getErrorMessage() {
        return message;
    }
}
