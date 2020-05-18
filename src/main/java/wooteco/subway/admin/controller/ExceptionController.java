package wooteco.subway.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import wooteco.subway.admin.exception.LineNotFoundException;
import wooteco.subway.admin.exception.StationNotFoundException;
import wooteco.subway.admin.exception.WrongPathException;

@ControllerAdvice
public class ExceptionController {

    public static final String SYSTEM_ERROR_MESSAGE = "시스템 에러";

    @ExceptionHandler(WrongPathException.class)
    public ResponseEntity<String> WrongPathExceptionHandler(WrongPathException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(LineNotFoundException.class)
    public ResponseEntity<String> LineNotFoundExceptionHandler(LineNotFoundException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(StationNotFoundException.class)
    public ResponseEntity<String> StationNotFoundExceptionHandler(StationNotFoundException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handler(Exception e) {
        return ResponseEntity.badRequest().body(SYSTEM_ERROR_MESSAGE);
    }
}
