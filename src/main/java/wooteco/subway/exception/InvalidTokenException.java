package wooteco.subway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class InvalidTokenException extends SubwayException {
    public InvalidTokenException() {
        super("유효하지 않은 토큰입니다.");
    }
}
