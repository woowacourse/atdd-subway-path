package wooteco.subway.dto;

import java.util.stream.Collectors;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.web.bind.MethodArgumentNotValidException;

public class ErrorResponse {
    private static final String DELIMITER = "," ;
    private String message;

    public ErrorResponse() {
    }

    public ErrorResponse(String message) {
        this.message = message;
    }

    public static ErrorResponse from(MethodArgumentNotValidException exception) {
        return new ErrorResponse(exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(DELIMITER)));
    }

    public String getMessage() {
        return message;
    }
}
