package wooteco.subway.ui;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Void> duplicatedNameFound() {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<Void> dataNotFoundException() {
        return ResponseEntity
                .internalServerError()
                .build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> unControlledExceptionFound() {
        return ResponseEntity.internalServerError().build();
    }
}
