package wooteco.exception.badrequest;

import org.springframework.http.HttpStatus;
import wooteco.exception.SubwayException;

public abstract class BadRequestException extends SubwayException {

    public BadRequestException(final String message) {
        super(message);
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.BAD_REQUEST;
    }
}
