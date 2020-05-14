package wooteco.subway.admin.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import wooteco.subway.admin.dto.ErrorResponse;

@RestControllerAdvice
public class ExceptionController {
    @ExceptionHandler(value = {RuntimeException.class})
    public ResponseEntity handleRuntimeException(RuntimeException e) {
        return new ResponseEntity(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
