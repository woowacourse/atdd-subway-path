package wooteco.exception.notfound;

import org.springframework.http.HttpStatus;
import wooteco.exception.SubwayException;

public abstract class NotFoundException extends SubwayException {

    public NotFoundException(String message) {
        super(message);
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.NOT_FOUND;
    }
}
