package wooteco.subway.admin.controller.exceptionhandler;

import java.util.NoSuchElementException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import wooteco.subway.admin.dto.ErrorCode;
import wooteco.subway.admin.dto.ErrorResponse;
import wooteco.subway.admin.exception.NotConnectEdgeException;
import wooteco.subway.admin.exception.SameStationException;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {
    @ExceptionHandler(SameStationException.class)
    public ResponseEntity<ErrorResponse> handleSameStation(SameStationException e) {
        return ResponseEntity.badRequest()
            .body(ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, e.getBindingResult()));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> handleNoSuchElement(NoSuchElementException e) {
        return ResponseEntity.badRequest()
            .body(ErrorResponse.of(e.getMessage()));
    }

    @ExceptionHandler(NotConnectEdgeException.class)
    public ResponseEntity<ErrorResponse> handleNotConnectEdge(NotConnectEdgeException e) {
        return ResponseEntity.badRequest()
            .body(ErrorResponse.of(e.getMessage()));
    }
}
