package wooteco.member.controller.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import wooteco.exception.ErrorMessage;
import wooteco.exception.HttpException;

@ControllerAdvice
public class AdviceController {

    @ExceptionHandler(HttpException.class)
    public ResponseEntity<ErrorMessage> handleException(HttpException e) {
        return ResponseEntity
            .status(e.getHttpStatus())
            .body(e.getErrorMessage());
    }
}
