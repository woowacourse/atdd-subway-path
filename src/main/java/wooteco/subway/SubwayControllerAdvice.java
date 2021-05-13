package wooteco.subway;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import wooteco.subway.exception.AuthorizationException;

@ControllerAdvice
public class SubwayControllerAdvice {
    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<String> duplicatedExceptionHandle(Exception e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

}
