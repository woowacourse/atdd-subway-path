package wooteco.subway.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
public class ErrorControllerAdvice {

    private final Logger logger = LoggerFactory.getLogger(ErrorControllerAdvice.class);

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> illegalStateExceptionHandler(Exception exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> illegalArgumentExceptionHandler(Exception exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> noSuchElementExceptionHandler(Exception exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Void> runtimeExceptionHandler(Exception exception) {
        logger.error("런타임 에러가 발생했습니다.", exception);
        return ResponseEntity.internalServerError().build();
    }
}
