package wooteco.subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import wooteco.subway.exception.InvalidInputException;

@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Void> duplicatedNameFound() {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<Void> invalidInputException() {
        return ResponseEntity
                .badRequest()
                .build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> unControlledExceptionFound() {
        return ResponseEntity.internalServerError().build();
    }
}
