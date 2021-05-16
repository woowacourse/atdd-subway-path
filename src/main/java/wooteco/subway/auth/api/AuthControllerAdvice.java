package wooteco.subway.auth.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.subway.auth.exception.AuthException;

@RestControllerAdvice
public class AuthControllerAdvice {

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<String> handleIllegalArgumentException(AuthException authException) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(authException.getMessage());
    }
}
