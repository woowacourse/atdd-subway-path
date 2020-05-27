package wooteco.subway.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import wooteco.subway.admin.domain.graph.PathNotFoundException;
import wooteco.subway.admin.dto.ErrorResponse;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(PathNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePathNotFoundException(PathNotFoundException e) {
        return ResponseEntity.badRequest().body(ErrorResponse.of(e));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(ErrorResponse.of(e));
    }

}
