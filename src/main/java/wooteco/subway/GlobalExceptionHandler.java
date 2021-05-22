package wooteco.subway;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.subway.auth.exception.AuthException;
import wooteco.subway.path.exception.PathException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({AuthException.class})
    public ResponseEntity<ErrorResponse> authException(RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler({PathException.class})
    public ResponseEntity<ErrorResponse> pathException(RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse(exception.getMessage()));
    }
}
