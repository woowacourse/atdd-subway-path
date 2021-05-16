package wooteco.subway;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.station.application.StationNotFoundException;

@ControllerAdvice(annotations = RestController.class)
public class AdviceException {

    @ExceptionHandler({StationNotFoundException.class})
    public ResponseEntity<String> handle(Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

}
