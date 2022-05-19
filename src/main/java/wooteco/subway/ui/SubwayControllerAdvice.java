package wooteco.subway.ui;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import wooteco.subway.exception.SubwayException;

@ControllerAdvice
public class SubwayControllerAdvice {

    @ExceptionHandler(SubwayException.class)
    public ResponseEntity<String> handleSubwayException(SubwayException exception) {
        return new ResponseEntity<>(exception.getMessage(), exception.getHttpStatus());
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<String> handleException() {
        return new ResponseEntity<>("내부 서버 에러입니다.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
