package wooteco.subway;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import wooteco.subway.exception.AuthorizationException;

@ControllerAdvice
public class SubwayControllerAdvice {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> exception(Exception e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("서버에서 요청을 처리하지 못했습니다.");
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<String> authorization(AuthorizationException e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BindingResult> methodArgumentNotValid(MethodArgumentNotValidException e) {
        return ResponseEntity
                .badRequest()
                .body(e.getBindingResult());
    }
}
