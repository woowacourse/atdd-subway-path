package wooteco.subway.admin.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.subway.admin.dto.ErrorResponse;
import wooteco.subway.admin.exception.NotFoundException;

import java.io.UnsupportedEncodingException;

@RestControllerAdvice
public class ExceptionHandlingController {
    private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @ExceptionHandler(UnsupportedEncodingException.class)
    public ResponseEntity<ErrorResponse> decodeFailureException(UnsupportedEncodingException error) {
        return ResponseEntity.badRequest().body(new ErrorResponse(error.getMessage()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> pathNotExistHandler(NotFoundException error) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(error.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> unexpectedExceptionHandler(Exception error) {
        logger.error(error.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("서버 오류가 발생했습니다. 지속해서 발생하면 관리자에게 문의 주세요."));
    }
}
