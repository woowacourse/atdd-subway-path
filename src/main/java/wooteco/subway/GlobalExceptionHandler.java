package wooteco.subway;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.subway.auth.exception.AuthException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({AuthException.class})
    public ResponseEntity<String> authException(RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getMessage());
    }
}
