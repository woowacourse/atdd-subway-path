package wooteco.subway.path.ui;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.subway.path.exceptions.NotReachableException;

@RestControllerAdvice
public class PathControllerAdvice {

    @ExceptionHandler(NotReachableException.class)
    public ResponseEntity<String> notReachableException(NotReachableException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(e.getMessage());
    }
}
