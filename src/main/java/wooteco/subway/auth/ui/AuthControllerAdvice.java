package wooteco.subway.auth.ui;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import wooteco.subway.auth.application.AuthorizationException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class AuthControllerAdvice {
    @ExceptionHandler({AuthorizationException.class})
    public ResponseEntity<Map<String, String>> handleException(final Exception e) {
        Map<String, String> body = new HashMap<>();
        body.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }
}
