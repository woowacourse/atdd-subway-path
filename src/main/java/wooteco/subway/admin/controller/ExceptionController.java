package wooteco.subway.admin.controller;

import java.security.InvalidParameterException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import wooteco.subway.admin.dto.ErrorResponse;
import wooteco.subway.admin.exception.NoExistLineException;
import wooteco.subway.admin.exception.NoExistLineStationException;
import wooteco.subway.admin.exception.NoExistPathException;
import wooteco.subway.admin.exception.NoExistPathTypeException;
import wooteco.subway.admin.exception.NoExistStationException;

@RestControllerAdvice
public class ExceptionController {
    @ExceptionHandler(value = {NoExistLineException.class, NoExistStationException.class,
        NoExistLineStationException.class, NoExistPathException.class,
        NoExistPathTypeException.class})
    public ResponseEntity<ErrorResponse> handleNoExistException(RuntimeException e) {
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()),
            HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {InvalidParameterException.class})
    public ResponseEntity<ErrorResponse> handleInvalidParameterException(
        InvalidParameterException e) {
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()),
            HttpStatus.BAD_REQUEST);
    }
}
