package wooteco.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionController.class);

    @ExceptionHandler(SubwayException.class)
    public ResponseEntity<String> subwayExceptionResponse(SubwayException exception) {
        LOGGER.error(exception.getMessage());

        return ResponseEntity.status(exception.status())
            .body(exception.getMessage());
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<String> duplicateKeyExceptionResponse(DuplicateKeyException exception) {
        LOGGER.error(exception.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(exception.getMessage());
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<String> emptyResultDataAccessExceptionResponse(EmptyResultDataAccessException exception) {
        LOGGER.error(exception.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(exception.getMessage());
    }
}
