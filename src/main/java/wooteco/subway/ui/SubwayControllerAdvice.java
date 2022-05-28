package wooteco.subway.ui;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.sql.SQLException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import wooteco.subway.dto.ErrorResponse;
import wooteco.subway.exception.DataNotExistException;
import wooteco.subway.exception.SubwayException;

@ControllerAdvice
public class SubwayControllerAdvice {

    @ExceptionHandler(DataNotExistException.class)
    public ResponseEntity<ErrorResponse> handleDataNotExistException(Exception exception) {
        ErrorResponse errorResponse = new ErrorResponse(exception.getMessage());
        return ResponseEntity.status(NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(SubwayException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(Exception exception) {
        ErrorResponse errorResponse = new ErrorResponse(exception.getMessage());
        return ResponseEntity.status(BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler({SQLException.class, DataAccessException.class})
    public ResponseEntity<ErrorResponse> handleSQLException() {
        ErrorResponse errorResponse = new ErrorResponse("서버 오류입니다.");
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Void> handleNoHandlerFoundException() {
        return ResponseEntity.status(NOT_FOUND).build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException() {
        ErrorResponse errorResponse = new ErrorResponse("잘못된 접근입니다.");
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
