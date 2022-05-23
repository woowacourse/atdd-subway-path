package wooteco.subway.ui;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import wooteco.subway.exception.EmptyResultException;
import wooteco.subway.exception.LowFareException;
import wooteco.subway.ui.dto.ExceptionResponse;

@ControllerAdvice
public class ControllerExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleIllegalArgumentException(IllegalArgumentException exception) {
        return ResponseEntity.badRequest().body(new ExceptionResponse(exception.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleEmptyResultException(EmptyResultException exception) {
        logger.error(exception.getMessage());
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleSqlException(DuplicateKeyException exception) {
        logger.error(exception.getMessage());
        return ResponseEntity.badRequest().body(new ExceptionResponse("이미 존재하는 데이터 입니다."));
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleNoSuchException(NoSuchElementException exception) {
        logger.error(exception.getMessage());
        return ResponseEntity.badRequest().body(new ExceptionResponse(exception.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<List<ExceptionResponse>> handleValidateException(MethodArgumentNotValidException exception) {
        logger.error(exception.getMessage());
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        List<ExceptionResponse> responses = fieldErrors.stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .map(ExceptionResponse::new)
            .collect(Collectors.toList());
        return ResponseEntity.badRequest().body(responses);
    }

    @ExceptionHandler({Exception.class, LowFareException.class})
    public ResponseEntity<ExceptionResponse> handleException(Exception exception) {
        logger.error(exception.getMessage());
        return ResponseEntity.internalServerError().body(new ExceptionResponse("서버 에러가 발생했습니다."));
    }
}
