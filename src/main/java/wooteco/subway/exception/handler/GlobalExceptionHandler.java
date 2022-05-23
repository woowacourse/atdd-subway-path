package wooteco.subway.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import wooteco.subway.exception.constant.CustomException;
import wooteco.subway.exception.constant.NotExistException;
import wooteco.subway.exception.dto.ErrorResult;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(NotExistException.class)
    private ErrorResult handleNotFoundException(Exception exception) {
        logError(exception);
        return new ErrorResult(NOT_FOUND, exception.getMessage());
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler({CustomException.class, DuplicateKeyException.class})
    private ErrorResult handleBadRequestException(Exception exception) {
        logError(exception);
        return new ErrorResult(BAD_REQUEST, exception.getMessage());
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    private ErrorResult handleException(Exception exception) {
        return new ErrorResult(BAD_REQUEST, "오류가 발생했습니다.");
    }

    /**
     * Spring 의 DefaultHandlerExceptionResolver 정책
     */

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler({
            TypeMismatchException.class,
            HttpMessageNotReadableException.class,
            HttpMessageNotWritableException.class,
            MissingServletRequestPartException.class,
            BindException.class,
            ServletRequestBindingException.class,
            MissingServletRequestParameterException.class
    })
    private ErrorResult handleSpringBadRequestException(Exception exception) {
        logError(exception);
        return new ErrorResult(BAD_REQUEST, "오류가 발생했습니다.");
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    private ErrorResult handleSpringNotFoundException(Exception exception) {
        logError(exception);
        return new ErrorResult(NOT_FOUND, "오류가 발생했습니다.");
    }

    @ResponseStatus(METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    private ErrorResult handleSpringMethodNotAllowedException(Exception exception) {
        logError(exception);
        return new ErrorResult(METHOD_NOT_ALLOWED, "오류가 발생했습니다.");
    }

    @ResponseStatus(NOT_ACCEPTABLE)
    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    private ErrorResult handleSpringNotAcceptableException(Exception exception) {
        logError(exception);
        return new ErrorResult(NOT_ACCEPTABLE, "오류가 발생했습니다.");
    }

    @ResponseStatus(UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    private ErrorResult handleSpringUnsupportedMediaTypeException(Exception exception) {
        logError(exception);
        return new ErrorResult(UNSUPPORTED_MEDIA_TYPE, "오류가 발생했습니다.");
    }

    @ResponseStatus(SERVICE_UNAVAILABLE)
    @ExceptionHandler(AsyncRequestTimeoutException.class)
    private ErrorResult handleSpringServiceUnavailableException(Exception exception) {
        logError(exception);
        return new ErrorResult(SERVICE_UNAVAILABLE, "오류가 발생했습니다.");
    }

    private static void logError(Exception exception) {
        log.error("error ! ", exception);
    }
}
