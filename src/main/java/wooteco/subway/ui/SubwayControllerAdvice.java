package wooteco.subway.ui;

import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.subway.exception.SubwayNotFoundException;
import wooteco.subway.exception.SubwayUnknownException;
import wooteco.subway.exception.SubwayValidationException;
import wooteco.subway.ui.dto.response.ExceptionResponse;

@RestControllerAdvice
public class SubwayControllerAdvice {

    private static final String UNKNOWN_EXCEPTION_MESSAGE = "확인되지 않은 예외가 발생했습니다. 관리자에게 문의해주세요.";

    @ExceptionHandler(SubwayValidationException.class)
    public ResponseEntity<ExceptionResponse> validationException(Exception e) {
        return new ResponseEntity<>(ExceptionResponse.from(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({BindException.class})
    public ResponseEntity<ExceptionResponse> requestValidationException(BindException e) {
        final String errorMessage = parseErrorMessage(e);

        return new ResponseEntity<>(ExceptionResponse.from(errorMessage), HttpStatus.BAD_REQUEST);
    }

    private String parseErrorMessage(BindException e) {
        return e.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> ((FieldError) error).getField() + " : " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
    }

    @ExceptionHandler(SubwayNotFoundException.class)
    public ResponseEntity<ExceptionResponse> notFoundException(Exception e) {
        return new ResponseEntity<>(ExceptionResponse.from(e.getMessage()), HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler({Exception.class, SubwayUnknownException.class})
    public ResponseEntity<ExceptionResponse> unknownException(Exception e) {
        return new ResponseEntity<>(ExceptionResponse.from(UNKNOWN_EXCEPTION_MESSAGE),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
