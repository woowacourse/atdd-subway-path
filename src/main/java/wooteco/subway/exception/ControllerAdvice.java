package wooteco.subway.exception;

import java.util.NoSuchElementException;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponse> handleIllegalInput() {
        return ResponseEntity.badRequest()
            .body(new ExceptionResponse("입력이 잘못되었습니다."));
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ExceptionResponse> handleArgumentNotValid(
        BindException exception) {
        return ResponseEntity.badRequest().body(new ExceptionResponse(
            Objects.requireNonNull(exception.getBindingResult().getFieldError()).getDefaultMessage()));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ExceptionResponse> handleNoData() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ExceptionResponse("리소스를 찾을 수 없습니다."));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException() {
        return ResponseEntity.internalServerError().body(new ExceptionResponse("서버에 문제가 발생했습니다. 다시 시도해주세요."));
    }
}
