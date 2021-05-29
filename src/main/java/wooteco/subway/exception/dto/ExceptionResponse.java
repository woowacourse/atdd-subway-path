package wooteco.subway.exception.dto;

import org.springframework.http.HttpStatus;

public class ExceptionResponse {
    private final String message;
    private final int status;

    public ExceptionResponse(String message, int status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }
}
