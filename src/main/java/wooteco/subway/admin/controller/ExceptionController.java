package wooteco.subway.admin.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import wooteco.subway.admin.dto.ErrorResponse;
import wooteco.subway.admin.exception.*;

@ControllerAdvice
public class ExceptionController {
    private static final String INTERNAL_SERVER_ERROR_MESSAGE = "예기치 못한 에러가 발생했습니다. 관리자에게 문의주세요.";

    @ExceptionHandler(value = {LineNotFoundException.class, LineStationNotFoundException.class,
            StationNotFoundException.class, SameDepatureArrivalStationException.class})
    public ResponseEntity<ErrorResponse> handleLineNotFoundException(RuntimeException exception) {
        return new ResponseEntity<>(new ErrorResponse(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(VerticesNotConnectedException.class)
    public ResponseEntity<ErrorResponse> handleVerticesNotConnectedException(VerticesNotConnectedException exception) {
        return new ResponseEntity<>(new ErrorResponse(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleInternalServerError(Exception exception) {
        System.out.println(exception.getMessage());
        return new ResponseEntity<>(new ErrorResponse(INTERNAL_SERVER_ERROR_MESSAGE),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
