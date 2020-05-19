package wooteco.subway.admin.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import wooteco.subway.admin.dto.ErrorResponse;
import wooteco.subway.admin.exception.DisconnectedStationException;
import wooteco.subway.admin.exception.DuplicateSourceTargetStationException;
import wooteco.subway.admin.exception.InvalidPathException;
import wooteco.subway.admin.exception.NotFoundLineException;
import wooteco.subway.admin.exception.NotFoundStationException;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(value = {InvalidPathException.class})
    public ResponseEntity<ErrorResponse> handleInvalidPathException(InvalidPathException e) {
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {NotFoundLineException.class})
    public ResponseEntity<ErrorResponse> handleNotFoundLineException(NotFoundLineException e) {
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {NotFoundStationException.class})
    public ResponseEntity<ErrorResponse> handleNotFoundStationException(
        NotFoundStationException e) {
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {DisconnectedStationException.class})
    public ResponseEntity<ErrorResponse> handleDisconnectedStationException(
        DisconnectedStationException e) {
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {DuplicateSourceTargetStationException.class})
    public ResponseEntity<ErrorResponse> handleDuplicateSourceTargetStationException(
        DuplicateSourceTargetStationException e) {
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()),
            HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
