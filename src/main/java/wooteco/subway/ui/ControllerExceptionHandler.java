package wooteco.subway.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

import org.springframework.web.bind.annotation.ModelAttribute;
import wooteco.subway.dto.ExceptionMessageDto;
import wooteco.subway.exception.EmptyResultException;

@ControllerAdvice
public class ControllerExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler
    public ResponseEntity<ExceptionMessageDto> handleIllegalArgumentException(IllegalArgumentException exception) {
        return ResponseEntity.badRequest().body(new ExceptionMessageDto(exception.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionMessageDto> handleEmptyResultException(EmptyResultException exception) {
        logger.error(exception.getMessage());
        return ResponseEntity.badRequest().body(new ExceptionMessageDto(exception.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionMessageDto> handleSqlException(DuplicateKeyException exception) {
        logger.error(exception.getMessage());
        return ResponseEntity.badRequest().body(new ExceptionMessageDto(exception.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<List<ExceptionMessageDto>> handleValidateException(MethodArgumentNotValidException exception) {
        logger.error(exception.getMessage());
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        return ResponseEntity.badRequest().body(fieldErrors.stream()
                            .map(DefaultMessageSourceResolvable::getDefaultMessage)
                            .map(ExceptionMessageDto::new)
                            .collect(Collectors.toList()));
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionMessageDto> handleException(Exception exception) {
        logger.error(exception.getMessage());
        return ResponseEntity.internalServerError().body(new ExceptionMessageDto(exception.getMessage()));
    }
}
