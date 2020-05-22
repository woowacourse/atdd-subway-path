package wooteco.subway.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import wooteco.subway.admin.domain.exception.InvalidFindPathException;
import wooteco.subway.admin.domain.exception.NoExistStationException;

@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler(NoExistStationException.class)
    public ResponseEntity<NoExistStationException> findingStationError(NoExistStationException exception) {
        return ResponseEntity.badRequest().body(exception);
    }

    @ExceptionHandler(InvalidFindPathException.class)
    public ResponseEntity<InvalidFindPathException> findingPathError(InvalidFindPathException exception) {
        return ResponseEntity.badRequest().body(exception);
    }
}
