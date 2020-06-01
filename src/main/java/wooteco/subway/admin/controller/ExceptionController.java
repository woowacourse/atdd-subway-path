package wooteco.subway.admin.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import wooteco.subway.admin.dto.ErrorResponse;
import wooteco.subway.admin.exception.DisconnectedStationException;
import wooteco.subway.admin.exception.DuplicateSourceTargetStationException;
import wooteco.subway.admin.exception.InvalidPathException;
import wooteco.subway.admin.exception.NotFoundLineException;
import wooteco.subway.admin.exception.NotFoundStationException;

@RestControllerAdvice
public class ExceptionController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(value = {InvalidPathException.class})
    public ResponseEntity<ErrorResponse> handleInvalidPathException(InvalidPathException e) {
        logger.error(e.getMessage(), e);
        return new ResponseEntity<>(new ErrorResponse("출발역과 도착역이 연결되어 있지 않습니다."),
            HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {NotFoundLineException.class})
    public ResponseEntity<ErrorResponse> handleNotFoundLineException(NotFoundLineException e) {
        logger.error(e.getMessage(), e);
        return new ResponseEntity<>(new ErrorResponse("존재하지 않는 노선입니다."), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {NotFoundStationException.class})
    public ResponseEntity<ErrorResponse> handleNotFoundStationException(
        NotFoundStationException e) {
        logger.error(e.getMessage(), e);
        return new ResponseEntity<>(new ErrorResponse("지하철역을 찾을 수 없습니다."), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {DisconnectedStationException.class})
    public ResponseEntity<ErrorResponse> handleDisconnectedStationException(
        DisconnectedStationException e) {
        logger.error(e.getMessage(), e);
        return new ResponseEntity<>(new ErrorResponse("출발역과 도착역이 올바른지 확인해주세요"),
            HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {DuplicateSourceTargetStationException.class})
    public ResponseEntity<ErrorResponse> handleDuplicateSourceTargetStationException(
        DuplicateSourceTargetStationException e) {
        logger.error(e.getMessage(), e);
        return new ResponseEntity<>(new ErrorResponse("출발역과 도착역은 같을 수 없습니다."),
            HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        logger.error(e.getMessage(), e);
        return new ResponseEntity<>(new ErrorResponse("오류가 발생했습니다."),
            HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
