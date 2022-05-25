package wooteco.subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.subway.dto.ErrorMessageResponse;

import java.util.Objects;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<ErrorMessageResponse> illegalArgumentExceptionHandler(final RuntimeException e) {
        final ErrorMessageResponse errorMessageResponse = new ErrorMessageResponse(e.getMessage());
        return ResponseEntity.badRequest().body(errorMessageResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessageResponse> exceptionHandler(final Exception e) {
        final ErrorMessageResponse errorMessageResponse = new ErrorMessageResponse("서버 에러가 발생했습니다.");
        return ResponseEntity.internalServerError().body(errorMessageResponse);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorMessageResponse> validExceptionHandler(final BindException e) {
        final ErrorMessageResponse errorMessageResponse = new ErrorMessageResponse(
                Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage());
        return ResponseEntity.badRequest().body(errorMessageResponse);
    }

}
