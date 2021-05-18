package wooteco.subway.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.subway.exception.AuthException;
import wooteco.subway.exception.ErrorMessage;
import wooteco.subway.exception.NotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorMessage> handle(NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getErrorMessage());
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ErrorMessage> handle(AuthException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getErrorMessage());
    }

}
