package wooteco.subway.admin.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.subway.admin.dto.ErrorResponse;
import wooteco.subway.admin.exception.LineNotFoundException;
import wooteco.subway.admin.exception.NotExistPathException;
import wooteco.subway.admin.exception.StationNotFoundException;

import java.io.UnsupportedEncodingException;

@RestControllerAdvice
public class ExceptionHandlingController {
    @ExceptionHandler({UnsupportedEncodingException.class})
    public ResponseEntity<ErrorResponse> decodeFailureException(UnsupportedEncodingException error) {
        return ResponseEntity.badRequest().body(new ErrorResponse(error.getMessage()));
    }

    @ExceptionHandler({NotExistPathException.class})
    public ResponseEntity<ErrorResponse> pathNotExistHandler(NotExistPathException error) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(error.getMessage()));
    }

    @ExceptionHandler({LineNotFoundException.class})
    public ResponseEntity<ErrorResponse> lineNotFoundHandler(LineNotFoundException error) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(error.getMessage()));
    }

    @ExceptionHandler({StationNotFoundException.class})
    public ResponseEntity<ErrorResponse> stationNotFoundHandler(StationNotFoundException error) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(error.getMessage()));
    }
}
