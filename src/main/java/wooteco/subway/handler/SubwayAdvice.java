package wooteco.subway.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import wooteco.subway.exception.AuthorizationException;

@ControllerAdvice
public class SubwayAdvice {
    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<String> handleAuthorization(Exception e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }
}
