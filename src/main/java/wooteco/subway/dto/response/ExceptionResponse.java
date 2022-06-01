package wooteco.subway.dto.response;

import org.springframework.validation.FieldError;

public class ExceptionResponse {

    private final String message;

    private ExceptionResponse(String message) {
        this.message = message;
    }

    private ExceptionResponse() {
        this(null);
    }

    public static ExceptionResponse of(Exception exception) {
        return new ExceptionResponse(exception.getMessage());
    }

    public static ExceptionResponse of(FieldError fieldError) {
        return new ExceptionResponse(fieldError.getDefaultMessage());
    }

    public String getMessage() {
        return message;
    }
}
