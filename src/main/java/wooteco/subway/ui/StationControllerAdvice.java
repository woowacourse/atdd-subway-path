package wooteco.subway.ui;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.subway.exception.ClientException;

import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice
public class StationControllerAdvice {

    @ExceptionHandler({ClientException.class})
    public ResponseEntity<String> handleIllegalArgumentException(ClientException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<String> handleMissingParams() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("입력 값을 모두 입력해주세요.");
    }
}
