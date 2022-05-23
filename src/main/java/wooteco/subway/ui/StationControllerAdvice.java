package wooteco.subway.ui;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.subway.exception.ClientException;

@RestControllerAdvice
public class StationControllerAdvice {

    @ExceptionHandler({ClientException.class})
    public ResponseEntity<String> handleIllegalArgumentException(ClientException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<String> handleMissingParams(BindException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getBindingResult()
                .getAllErrors()
                .get(0)
                .getDefaultMessage());
    }
}
