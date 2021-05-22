package wooteco.subway.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends HttpException {

    private static final HttpStatus STATUS_CODE = HttpStatus.UNAUTHORIZED;
    private static final String ERROR_MESSAGE = "인증되지 않았습니다.";

    public UnauthorizedException() {
        this(ERROR_MESSAGE);
    }

    public UnauthorizedException(String message) {
        super(STATUS_CODE, message);
    }
}
