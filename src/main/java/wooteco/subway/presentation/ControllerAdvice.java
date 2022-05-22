package wooteco.subway.presentation;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.subway.dto.ErrorResponse;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> globalException(Exception exception) {
        return ResponseEntity.internalServerError().body(new ErrorResponse(List.of(exception.getMessage())));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> illegalArgumentException(IllegalArgumentException exception) {
        return ResponseEntity.badRequest().body(new ErrorResponse(List.of(exception.getMessage())));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> noSuchElementException(NoSuchElementException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(List.of(exception.getMessage())));
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodArgumentNotValidException(MethodArgumentNotValidException exception) {
        List<ObjectError> errors = exception.getBindingResult().getAllErrors();
        List<String> messages = errors.stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        return ResponseEntity.badRequest().body(new ErrorResponse(messages));
    }

}
