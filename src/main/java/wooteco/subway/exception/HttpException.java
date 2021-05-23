package wooteco.subway.exception;

import org.springframework.http.HttpStatus;

public class HttpException extends RuntimeException {

    private static final String EMPTY_BODY = "";

    private final HttpStatus status;
    private final String body;

    public HttpException(HttpStatus status) {
        this(status, EMPTY_BODY);
    }

    public HttpException(HttpStatus status, String body) {
        this.status = status;
        this.body = body;
    }

    public HttpStatus status() {
        return status;
    }

    public String body() {
        return body;
    }
}
