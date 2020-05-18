package wooteco.subway.admin.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidSubwayPathException extends RuntimeException {
    public InvalidSubwayPathException(final String message) {
        super(message);
    }
}
