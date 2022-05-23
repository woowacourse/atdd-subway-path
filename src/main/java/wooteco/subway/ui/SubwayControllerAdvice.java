package wooteco.subway.ui;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import wooteco.subway.ui.dto.ErrorMessage;

@ControllerAdvice
public class SubwayControllerAdvice {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler
    public ResponseEntity<ErrorMessage> handle(IllegalArgumentException exception) {
        logger.error(exception.getMessage(), exception);
        ErrorMessage message = new ErrorMessage(exception.getMessage());
        return ResponseEntity.badRequest().body(message);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorMessage> handleSqlException(DuplicateKeyException exception) {
        logger.error(exception.getMessage(), exception);
        ErrorMessage message = new ErrorMessage("이미 존재하는 데이터 입니다.");
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorMessage> handleValidException(MethodArgumentNotValidException exception) {
        String errorMessage = exception.getBindingResult().getAllErrors().get(0)
            .getDefaultMessage();
        logger.error(exception.getMessage(), exception);
        ErrorMessage message = new ErrorMessage(errorMessage);
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }
}
