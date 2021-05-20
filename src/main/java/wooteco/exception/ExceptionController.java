package wooteco.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {

    private static final Logger log = LoggerFactory.getLogger(ExceptionController.class);

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<String> duplicateExceptionResponse(DuplicateKeyException e) {
        String errorMessage = "데이터가 중복됩니다.";
        log.error(errorMessage);

        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(errorMessage);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> notFoundExceptionResponse(NotFoundException e) {
        log.error(e.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(e.getMessage());
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<String> voidLineDeleteExceptionResponse(EmptyResultDataAccessException e) {
        log.error(e.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(e.getMessage());
    }

    @ExceptionHandler(InvalidDistanceException.class)
    public ResponseEntity<String> invalidDistanceExceptionResponse(InvalidDistanceException e) {
        log.error(e.getMessage());

        return ResponseEntity.badRequest()
            .body(e.getMessage());
    }

    @ExceptionHandler({NullIdException.class, NullNameException.class, NullColorException.class})
    public ResponseEntity<String> nullExceptionResponse(NullException e) {
        log.error(e.getMessage());

        return ResponseEntity.badRequest()
            .body(e.getMessage());
    }

    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<String> duplicatedStationExceptionResponse(DuplicateException e) {
        log.error(e.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> methodArgumentNotValidExceptionResponse(MethodArgumentNotValidException e) {
        log.error(e.getMessage());

        return ResponseEntity.badRequest()
            .body(e.getMessage());
    }

    @ExceptionHandler(InvalidSectionOnLineException.class)
    public ResponseEntity<String> alreadyExistedStationsOnLineExceptionResponse(InvalidSectionOnLineException e) {
        log.error(e.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> illegalArgumentExceptionResponse(IllegalArgumentException e) {
        log.error(e.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(e.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<String> unauthorizedException(UnauthorizedException e) {
        log.error(e.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(e.getMessage());
    }
}
