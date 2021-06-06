package wooteco.exception.unahuthorized;

import org.springframework.http.HttpStatus;
import wooteco.exception.SubwayException;

public abstract class UnauthorizedException extends SubwayException {

    public UnauthorizedException(String message) {
        super(message);
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.UNAUTHORIZED;
    }
}
