package wooteco.subway;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import wooteco.subway.auth.exception.UserLoginFailException;

@ControllerAdvice
public class SubwayAdvice {

    @ExceptionHandler(UserLoginFailException.class)
    public ResponseEntity<String> handleUserLoginFailException(UserLoginFailException exception) {
        return ResponseEntity.status(exception.getStatusCode()).body(exception.getMessage());
    }
}
