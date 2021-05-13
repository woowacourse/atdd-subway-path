package wooteco.subway.auth.ui;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.subway.exception.auth.LoginFailException;

@RestControllerAdvice
public class AuthControllerAdvice {

    @ExceptionHandler(LoginFailException.class)
    public ResponseEntity<String> notFoundEmailException(LoginFailException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .body(e.getMessage());
    }
}
