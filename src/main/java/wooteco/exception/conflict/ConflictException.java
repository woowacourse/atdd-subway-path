package wooteco.exception.conflict;

import org.springframework.http.HttpStatus;
import wooteco.exception.SubwayException;

public abstract class ConflictException extends SubwayException {

    public ConflictException(String message) {
        super(message);
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.CONFLICT;
    }
}
