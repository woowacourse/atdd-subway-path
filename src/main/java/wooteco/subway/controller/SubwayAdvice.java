package wooteco.subway.controller;

import java.util.NoSuchElementException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SubwayAdvice {
    @ExceptionHandler({Exception.class})
    public ResponseEntity<Void> handle(Exception e) {
        return ResponseEntity.internalServerError().build();
    }

    @ExceptionHandler({IllegalArgumentException.class, NoSuchElementException.class})
    public ResponseEntity<String> handle(RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
