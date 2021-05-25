package wooteco.subway;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import wooteco.subway.auth.exception.UserLoginFailException;
import wooteco.subway.exceptions.SubWayCustomException;

@ControllerAdvice
public class SubwayAdvice {

    @ExceptionHandler(UserLoginFailException.class)
    public ResponseEntity<String> handleUserLoginFailException(UserLoginFailException exception) {
        return ResponseEntity.status(exception.getStatusCode()).body(exception.getMessage());
    }

    @ExceptionHandler(SubWayCustomException.class)
    public ResponseEntity<String> handleSubWayCustomException(SubWayCustomException exception) {
        return ResponseEntity.status(exception.status()).body(exception.message());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception exception) {
        System.out.println(exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .body("알 수 없는 에러가 발생했습니다.");
    }
}
