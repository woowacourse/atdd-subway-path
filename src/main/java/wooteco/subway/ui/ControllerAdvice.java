package wooteco.subway.ui;

import javax.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.subway.dto.response.ErrorResponse;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler({IllegalStateException.class, ConstraintViolationException.class,
            MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponse> duplicateStation(final Exception exception) {
        return new ResponseEntity<>(new ErrorResponse(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({BindException.class})
    public ResponseEntity<ErrorResponse> requestDataInvalid(final BindException e) {
        final String errorMessage = e.getBindingResult()
                .getAllErrors()
                .get(0)
                .getDefaultMessage();
        return new ResponseEntity<>(new ErrorResponse(errorMessage), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({Exception.class, RuntimeException.class})
    public ResponseEntity<ErrorResponse> unexpectedError(Exception e) {
        e.printStackTrace();
        return new ResponseEntity<>(new ErrorResponse("실행할 수 없는 명령입니다."), HttpStatus.BAD_REQUEST);
    }
}
