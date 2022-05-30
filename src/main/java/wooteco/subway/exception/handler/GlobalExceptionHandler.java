package wooteco.subway.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import wooteco.subway.exception.constant.ClientException;
import wooteco.subway.exception.constant.NotExistException;
import wooteco.subway.exception.dto.ErrorResult;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(NotExistException.class)
    private ErrorResult handleNotFoundException(Exception exception) {
        logError(exception);
        return new ErrorResult(NOT_FOUND, exception.getMessage());
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler({ClientException.class, DuplicateKeyException.class})
    private ErrorResult handleBadRequestException(Exception exception) {
        logError(exception);
        return new ErrorResult(BAD_REQUEST, exception.getMessage());
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    private ErrorResult handleException(Exception exception) {
        return new ErrorResult(INTERNAL_SERVER_ERROR, "오류가 발생했습니다.");
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex,
                                                             Object body,
                                                             HttpHeaders headers,
                                                             HttpStatus status,
                                                             WebRequest request) {
        return ResponseEntity.status(status)
                .body(new ErrorResult(status, "오류가 발생했습니다."));
    }

    private static void logError(Exception exception) {
        log.error("error ! ", exception);
    }
}
