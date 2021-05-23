package wooteco;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.dto.ErrorResponse;
import wooteco.dto.ParameterValidationErrorResponse;
import wooteco.exception.RequiredParameterValidationException;

@Order
@RestControllerAdvice
public class ControllerAdvice {
    @ExceptionHandler(RequiredParameterValidationException.class)
    public ResponseEntity<ParameterValidationErrorResponse> handleParameterValidationErrorResponse(RequiredParameterValidationException e) {
        return ResponseEntity.badRequest()
                .body(new ParameterValidationErrorResponse(
                        e.getMessage(),
                        e.getCauses()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(
                        e.getMessage()));
    }
}
