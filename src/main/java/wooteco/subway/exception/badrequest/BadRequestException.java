package wooteco.subway.exception.badrequest;

import org.springframework.http.HttpStatus;
import wooteco.subway.exception.SubwayException;

public class BadRequestException extends SubwayException {
    private static final HttpStatus BAD_REQUEST = HttpStatus.BAD_REQUEST;

    public BadRequestException(String message) {
        super(message, BAD_REQUEST);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause, BAD_REQUEST);
    }
}
