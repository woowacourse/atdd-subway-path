package wooteco.subway.admin.controller;

import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
public class ExceptionAdvice {
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException e) {
        return ResponseEntity.badRequest()
                .body(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest()
                .body(e.getMessage());
    }

    @ExceptionHandler(DbActionExecutionException.class)
    public ResponseEntity<String> handleDbActionExecutionException(DbActionExecutionException e) {
        return ResponseEntity.badRequest()
                .body("DB 오류입니다.");
    }
}
