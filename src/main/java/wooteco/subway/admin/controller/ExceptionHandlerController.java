package wooteco.subway.admin.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import wooteco.subway.admin.dto.response.ErrorResponse;
import wooteco.subway.admin.exception.InvalidLineException;
import wooteco.subway.admin.exception.InvalidPathException;
import wooteco.subway.admin.exception.InvalidStationException;
import wooteco.subway.admin.exception.NotFoundLineException;

@RestControllerAdvice
public class ExceptionHandlerController {
    private Logger logger = LoggerFactory.getLogger(ExceptionHandlerController.class);

    @ExceptionHandler(value = InvalidLineException.class)
    public ResponseEntity<ErrorResponse> HandleInvalidLineException(InvalidLineException e) {
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(value = InvalidPathException.class)
    public ResponseEntity<ErrorResponse> HandleInvalidPathException(InvalidPathException e) {
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(value = InvalidStationException.class)
    public ResponseEntity<ErrorResponse> HandleInvalidStationException(InvalidStationException e) {
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(value = NotFoundLineException.class)
    public ResponseEntity<ErrorResponse> HandleNotFoundLineException(NotFoundLineException e) {
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(value = Exception.class)
    public void HandleException(Exception e) {
        logger.error("error", e);
    }
}
