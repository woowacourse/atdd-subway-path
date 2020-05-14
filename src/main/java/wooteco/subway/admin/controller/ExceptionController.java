package wooteco.subway.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
public class ExceptionController {
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<String> errorHandle(MethodArgumentNotValidException e) {
        return ResponseEntity.status(500).body(e.getMessage());
    }
}
