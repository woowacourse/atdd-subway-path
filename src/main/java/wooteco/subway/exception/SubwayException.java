package wooteco.subway.exception;

import org.springframework.http.HttpStatus;

public abstract class SubwayException extends RuntimeException {

    private final HttpStatus httpStatus;

    public SubwayException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public final HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
