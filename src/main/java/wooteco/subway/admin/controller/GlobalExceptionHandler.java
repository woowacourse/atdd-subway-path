package wooteco.subway.admin.controller;

import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import wooteco.subway.admin.dto.ErrorResponse;
import wooteco.subway.admin.exception.InvalidPathTypeException;
import wooteco.subway.admin.exception.NotFoundPathException;
import wooteco.subway.admin.exception.NotFoundStationException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handleBindException(BindException e) {
        ErrorResponse errorResponse = new ErrorResponse(
                Objects.requireNonNull(e.getFieldError()).getDefaultMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(InvalidPathTypeException.class)
    public ResponseEntity<ErrorResponse> handleInvalidPathTypeException(
            InvalidPathTypeException e) {
        ErrorResponse errorResponse = new ErrorResponse("유효하지 않는 경로 타입입니다.");
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(NotFoundStationException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundStationException(
            NotFoundStationException e) {
        ErrorResponse errorResponse = new ErrorResponse("입력한 역을 찾을 수 없습니다");
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(NotFoundPathException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundPathException(NotFoundPathException e) {
        ErrorResponse errorResponse = new ErrorResponse("입력한 경로를 찾을 수 없습니다");
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        ErrorResponse errorResponse = new ErrorResponse("서버에서 오류가 발생했습니다");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
