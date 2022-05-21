package wooteco.subway.ui;


import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import wooteco.subway.ui.dto.MessageResponse;

@ControllerAdvice
public class SubwayControllerAdvice {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler
    public ResponseEntity<MessageResponse> handle(IllegalArgumentException exception) {
        logger.error(exception.getMessage());
        return ResponseEntity.badRequest().body(new MessageResponse(exception.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<MessageResponse> handleSqlException(DuplicateKeyException exception) {
        logger.error(exception.getMessage());
        return ResponseEntity.badRequest().body(new MessageResponse("이미 존재하는 데이터 입니다."));
    }

    @ExceptionHandler
    public ResponseEntity<MessageResponse> handleValidException(MethodArgumentNotValidException exception) {
        String errorMessage = exception.getBindingResult().getAllErrors().get(0)
            .getDefaultMessage();
        logger.error(errorMessage);
        return ResponseEntity.badRequest().body(new MessageResponse(errorMessage));
    }
}
