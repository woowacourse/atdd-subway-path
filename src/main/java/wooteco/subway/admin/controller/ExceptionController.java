package wooteco.subway.admin.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import wooteco.subway.admin.dto.ErrorResponse;
import wooteco.subway.admin.exception.InvalidSubwayPathException;
import wooteco.subway.admin.exception.LineNotFoundException;
import wooteco.subway.admin.exception.LineStationNotFoundException;
import wooteco.subway.admin.exception.StationNotFoundException;

@RestControllerAdvice
public class ExceptionController {
    @ExceptionHandler(value = {LineNotFoundException.class, LineStationNotFoundException.class,
        StationNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleSubwayElementNotFoundException(
        RuntimeException exception) {
        return new ResponseEntity<>(new ErrorResponse(exception.getMessage()),
            HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = InvalidSubwayPathException.class)
    public ResponseEntity<ErrorResponse> handleSubwayPathException(
        InvalidSubwayPathException exception) {
        return new ResponseEntity<>(new ErrorResponse(exception.getMessage()),
            HttpStatus.NOT_FOUND);
    }
}
