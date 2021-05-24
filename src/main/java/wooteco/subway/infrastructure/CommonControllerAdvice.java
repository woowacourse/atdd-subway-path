package wooteco.subway.infrastructure;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CommonControllerAdvice {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e)  {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
