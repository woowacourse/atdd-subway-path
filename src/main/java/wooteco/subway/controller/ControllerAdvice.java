package wooteco.subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.subway.controller.dto.ErrorResponse;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler({Exception.class, RuntimeException.class})
    public ResponseEntity<ErrorResponse> unexpectedError() {
        return ResponseEntity.badRequest().body(ErrorResponse.from("실행할 수 없는 명령입니다."));
    }

    @ExceptionHandler({IllegalStateException.class})
    public ResponseEntity<ErrorResponse> duplicateStation(final Exception exception) {
        return ResponseEntity.badRequest().body(ErrorResponse.from(exception));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(final Exception exception) {
        ErrorResponse errorResponse = ErrorResponse.from(exception);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(final MethodArgumentNotValidException exception) {
        ErrorResponse errorResponse = ErrorResponse.from(exception);
        return ResponseEntity.badRequest().body(errorResponse);
    }
}
