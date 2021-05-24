package wooteco.subway.exception.unauthorization;

import org.springframework.http.HttpStatus;
import wooteco.subway.exception.SubwayException;

public class UnAuthorizationException extends SubwayException {
    private static final HttpStatus UNAUTHORIZED = HttpStatus.UNAUTHORIZED;

    public UnAuthorizationException(String message) {
        super(message, UNAUTHORIZED);
    }

    public UnAuthorizationException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }

    public UnAuthorizationException(String message, Throwable cause, HttpStatus httpStatus) {
        super(message, cause, httpStatus);
    }
}
