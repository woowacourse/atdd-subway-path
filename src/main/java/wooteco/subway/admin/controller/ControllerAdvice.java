package wooteco.subway.admin.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import wooteco.subway.admin.dto.response.ApiError;
import wooteco.subway.admin.exception.DuplicateNameException;
import wooteco.subway.admin.exception.EntityNotFoundException;
import wooteco.subway.admin.exception.LineStationException;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError notFoundException(EntityNotFoundException enfe) {
        return new ApiError(HttpStatus.NOT_FOUND, enfe.getMessage());
    }

    @ExceptionHandler({DuplicateNameException.class, LineStationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError duplicateNameException(IllegalArgumentException ie) {
        return new ApiError(HttpStatus.BAD_REQUEST, ie.getMessage());
    }
}
