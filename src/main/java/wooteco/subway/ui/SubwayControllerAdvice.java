package wooteco.subway.ui;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.subway.exception.DuplicateLineColorException;
import wooteco.subway.exception.DuplicateLineNameException;
import wooteco.subway.exception.DuplicateStationNameException;
import wooteco.subway.ui.dto.response.ExceptionResponse;

@RestControllerAdvice
public class SubwayControllerAdvice {

    private static final String UNKNOWN_EXCEPTION_MESSAGE = "확인되지 않은 예외가 발생했습니다. 잠시 후 다시 시도해주세요.";

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> unknownException(Exception e) {
        return new ResponseEntity<>(ExceptionResponse.from(UNKNOWN_EXCEPTION_MESSAGE),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({DuplicateLineNameException.class, DuplicateLineColorException.class})
    public ResponseEntity<ExceptionResponse> handle(RuntimeException exception) {
        return new ResponseEntity<>(ExceptionResponse.from(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handle(DuplicateStationNameException exception) {
        return new ResponseEntity<>(ExceptionResponse.from(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
