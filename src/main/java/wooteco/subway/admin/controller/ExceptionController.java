package wooteco.subway.admin.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import wooteco.subway.admin.dto.ErrorResponse;
import wooteco.subway.admin.exception.*;

@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler(value = {LineNotFoundException.class, LineStationNotFoundException.class,
            StationNotFoundException.class, SameDepatureArrivalStationException.class})
    public ResponseEntity<ErrorResponse> handleLineNotFoundException(RuntimeException exception) {
        return new ResponseEntity<>(new ErrorResponse(exception.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(VerticesNotConnectedException.class)
    public ResponseEntity<ErrorResponse> handleVerticesNotConnectedException(VerticesNotConnectedException exception) {
        return new ResponseEntity<>(new ErrorResponse(exception.getMessage()), HttpStatus.NOT_FOUND);
    }
}
