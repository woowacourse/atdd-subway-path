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

    @ExceptionHandler(
        {
            InvalidLineException.class,
            InvalidPathException.class,
            InvalidStationException.class,
            NotFoundLineException.class
        })
    public ResponseEntity<ErrorResponse> HandleCustomException(InvalidLineException e) {
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> HandleException(Exception e) {
        logger.error("error", e);
        return ResponseEntity.badRequest().body(new ErrorResponse("알 수 없는 에러입니다."));
    }
}
