package wooteco.subway.path.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import wooteco.subway.exception.NotExistingPathException;

@ControllerAdvice
public class PathControllerAdvice {

    @ExceptionHandler(NotExistingPathException.class)
    public ResponseEntity<String> notExistingPath(NotExistingPathException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
