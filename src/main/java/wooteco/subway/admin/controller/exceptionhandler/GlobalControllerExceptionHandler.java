package wooteco.subway.admin.controller.exceptionhandler;

import java.util.NoSuchElementException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import wooteco.subway.admin.dto.ExceptionResponse;
import wooteco.subway.admin.exception.NotConnectEdgeException;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionResponse> handleRunTime(RuntimeException e) {
        return ResponseEntity.badRequest()
            .body(new ExceptionResponse(e.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponse> handleConflict(IllegalArgumentException e) {
        return ResponseEntity.badRequest()
            .body(new ExceptionResponse(e.getMessage()));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ExceptionResponse> handleNoSuchElement(NoSuchElementException e){
        return ResponseEntity.badRequest()
            .body(new ExceptionResponse(e.getMessage()));
    }

    @ExceptionHandler(NotConnectEdgeException.class)
    public ResponseEntity<ExceptionResponse> handleNotConnectEdge(NotConnectEdgeException e){
        return ResponseEntity.badRequest()
            .body(new ExceptionResponse(e.getMessage()));
    }
}
