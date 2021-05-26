package wooteco.subway;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import wooteco.subway.auth.exception.AuthorizationException;
import wooteco.subway.exception.BusinessRelatedException;
import wooteco.subway.exception.ObjectNotFoundException;
import wooteco.subway.exception.dto.ErrorResponse;

@ControllerAdvice
public class SpecificExceptionHandler {

    @ExceptionHandler(BusinessRelatedException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BusinessRelatedException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<ErrorResponse> handleAuthorizationException(AuthorizationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(ObjectNotFoundException e) {
        return ResponseEntity.status(HttpStatus. NOT_FOUND)
            .body(new ErrorResponse(e.getMessage()));
    }
}
