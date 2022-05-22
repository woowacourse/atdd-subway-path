package wooteco.subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import wooteco.subway.exception.SubwayException;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(SubwayException.class)
    public ResponseEntity<String> subwayException(SubwayException exception) {
        return ResponseEntity.status(exception.getHttpStatus()).body(exception.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> handleError(Exception e) {
        if (e.getMessage() != null) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.internalServerError().body("예기치 못한 예외가 발생했습니다.");
    }
}
