package wooteco.subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import wooteco.subway.dto.ExceptionResponse;
import wooteco.subway.exception.InvalidInputException;

@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponse> duplicatedNameFound(IllegalArgumentException illegalArgumentException) {
        return ResponseEntity.badRequest().body(new ExceptionResponse(illegalArgumentException.getMessage()));
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ExceptionResponse> invalidInputException(InvalidInputException invalidInputException) {
        return ResponseEntity
                .badRequest()
                .body(new ExceptionResponse(invalidInputException.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> unControlledExceptionFound(Exception exception) {
        return ResponseEntity.internalServerError().body(new ExceptionResponse(exception.getMessage()));
    }
}
