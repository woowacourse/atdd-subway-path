package wooteco.member.controller.advice;

import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import wooteco.member.exception.EmailNotFoundException;

import java.io.InvalidClassException;

@ControllerAdvice
public class MemberControllerAdvice {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> unpredictableException(Exception error) {
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({InvalidClassException.class, JwtException.class})
    public ResponseEntity<String> handleException(Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<String> handleException(EmailNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
    }
}
