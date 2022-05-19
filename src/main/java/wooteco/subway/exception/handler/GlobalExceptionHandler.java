package wooteco.subway.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.subway.exception.constant.CustomException;
import wooteco.subway.exception.constant.NotExistException;
import wooteco.subway.exception.dto.ErrorResult;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(NotExistException.class)
    private ErrorResult handleExceptionToNotFound(Exception exception) {
        logError(exception);
        return new ErrorResult(NOT_FOUND, exception.getMessage());
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler({CustomException.class, DuplicateKeyException.class})
    private ErrorResult handleExceptionToBadRequest(Exception exception) {
        logError(exception);
        return new ErrorResult(BAD_REQUEST, exception.getMessage());
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    private ErrorResult handleExceptionToInternalServerError(Exception exception) {
        logError(exception);
        return new ErrorResult(INTERNAL_SERVER_ERROR);
    }

    private void logError(Exception exception) {
        log.error("error: {}", exception);
    }
}
