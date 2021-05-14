package wooteco.subway.auth.ui;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.subway.auth.application.AuthorizedException;

@RestControllerAdvice
public class AuthControllerAdvice {

    @ExceptionHandler(AuthorizedException.class)
    public ResponseEntity<Void> handleInvalidAuthorized(){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
