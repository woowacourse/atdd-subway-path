package wooteco.subway.ui.controller;

import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.subway.dto.response.ExceptionResponse;

@RestControllerAdvice
public class ExceptionAdviser {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ExceptionResponse> handleInvalidRequest(final BindingResult bindingResult) {
        final List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        final FieldError mainError = fieldErrors.get(0);
        return ResponseEntity.badRequest().body(ExceptionResponse.of(mainError));
    }

    @ExceptionHandler({IllegalStateException.class, IllegalArgumentException.class})
    public ResponseEntity<ExceptionResponse> handleRequestError(Exception exception) {
        return ResponseEntity.badRequest().body(ExceptionResponse.of(exception));
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ExceptionResponse> handleServerError(Exception exception) {
        return ResponseEntity.internalServerError().body(ExceptionResponse.of(exception));
    }
}
