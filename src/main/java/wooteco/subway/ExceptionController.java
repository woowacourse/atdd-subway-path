package wooteco.subway;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import wooteco.subway.exception.badRequest.BadRequestException;
import wooteco.subway.exception.notFound.NotFoundException;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity notFoundException(RuntimeException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity badRequest(BadRequestException badRequest) {
        return ResponseEntity.badRequest().body(badRequest.errorResponse());
    }
}
