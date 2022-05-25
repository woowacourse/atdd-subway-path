package wooteco.subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import wooteco.subway.exception.DuplicationException;
import wooteco.subway.exception.NoReachableStationException;
import wooteco.subway.exception.NotFoundException;
import wooteco.subway.exception.SectionException;

@ControllerAdvice
public class ErrorControllerAdvice {

    public static final String SERVER_ERROR_MESSAGE = "서버 내부 오류가 발생했습니다.";

    @ExceptionHandler({DuplicationException.class, NotFoundException.class, SectionException.class,
            NoReachableStationException.class})
    public ResponseEntity<String> illegalArgumentExceptionHandler(Exception exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handle() {
        return ResponseEntity.internalServerError().body(SERVER_ERROR_MESSAGE);
    }

}
