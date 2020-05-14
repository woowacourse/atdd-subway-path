package wooteco.subway.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.subway.admin.dto.ErrorResponse;
import wooteco.subway.admin.exception.NotExistPathException;
import wooteco.subway.admin.exception.NotFoundException;

import java.io.UnsupportedEncodingException;


@RestControllerAdvice
public class ExceptionHandlingController {

    @ExceptionHandler({UnsupportedEncodingException.class})
    public ResponseEntity<ErrorResponse> errorDuplicatedValue(UnsupportedEncodingException error) {
        return ResponseEntity.badRequest().body(new ErrorResponse(error.getMessage()));
    }

    @ExceptionHandler({NotExistPathException.class})
    public ResponseEntity<ErrorResponse> errorDuplicatedValue(NotExistPathException error) {
        return ResponseEntity.badRequest().body(new ErrorResponse(error.getMessage()));
    }

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<ErrorResponse> errorDuplicatedValue(NotFoundException error) {
        return ResponseEntity.badRequest().body(new ErrorResponse(error.getMessage()));
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorResponse> errorDuplicatedValue(Exception error) {
        return ResponseEntity.badRequest().body(new ErrorResponse(error.getMessage()));
    }


}
