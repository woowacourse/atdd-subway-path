package wooteco.subway.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.subway.member.exception.HttpException;
import wooteco.subway.member.exception.message.ErrorMessage;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(HttpException.class)
    public ResponseEntity<ErrorMessage> handle(HttpException e) {
        return ResponseEntity.status(e.getHttpStatus()).body(e.getErrorMessage());
    }
}
