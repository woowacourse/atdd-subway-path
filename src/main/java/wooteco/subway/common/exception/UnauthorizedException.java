package wooteco.subway.common.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends HttpException {

    private static final HttpStatus STATUS_CODE = HttpStatus.UNAUTHORIZED;

    public UnauthorizedException() {
        super(STATUS_CODE);
    }

    public UnauthorizedException(String message) {
        super(STATUS_CODE, message);
    }
}
