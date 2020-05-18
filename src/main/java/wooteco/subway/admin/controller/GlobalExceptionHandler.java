package wooteco.subway.admin.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import wooteco.subway.admin.dto.res.ErrorResponse;
import wooteco.subway.admin.exception.BusinessException;
import wooteco.subway.admin.exception.ErrorCode;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> invalidInputException(MethodArgumentNotValidException e) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.INVALID_REQUEST, e.getBindingResult());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> invalidRequestMethodException(HttpRequestMethodNotSupportedException e) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.INVALID_REQUEST_METHOD);
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(ErrorCode.INVALID_REQUEST_METHOD.getStatus()));
    }

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorResponse> invalidBusinessException(BusinessException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), e.getStatus());
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(errorResponse.getStatus()));
    }
}
