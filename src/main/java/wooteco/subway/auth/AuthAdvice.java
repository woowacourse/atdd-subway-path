package wooteco.subway.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.subway.auth.exception.InvalidMemberException;
import wooteco.subway.auth.exception.InvalidTokenException;

@RestControllerAdvice("wooteco.subway.auth")
public class AuthAdvice {

    @ExceptionHandler(Exception.class)
    private ResponseEntity<Void> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @ExceptionHandler(InvalidMemberException.class)
    private ResponseEntity<Void> handleInvalidMemberException(InvalidMemberException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(InvalidTokenException.class)
    private ResponseEntity<Void> handleInvalidTokenException(InvalidTokenException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}
