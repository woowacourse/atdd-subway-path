package wooteco.subway.ui;

import java.util.stream.Collectors;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import wooteco.subway.application.exception.DuplicateException;
import wooteco.subway.application.exception.NotFoundException;
import wooteco.subway.application.exception.RidiculousAgeException;
import wooteco.subway.application.exception.UnaddableSectionException;
import wooteco.subway.application.exception.UndeletableSectionException;
import wooteco.subway.application.exception.UnreachablePathException;
import wooteco.subway.domain.exception.BlankArgumentException;
import wooteco.subway.domain.exception.UnmergeableException;
import wooteco.subway.domain.exception.UnsplittableException;
import wooteco.subway.dto.ErrorResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BindException.class)
    private ResponseEntity<ErrorResponse> handleValidException(BindException e) {
        String message = e.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(","));

        return ResponseEntity.badRequest().body(new ErrorResponse(message));
    }

    @ExceptionHandler({DuplicateException.class, BlankArgumentException.class,
            UnaddableSectionException.class, UndeletableSectionException.class, UnreachablePathException.class,
            UnsplittableException.class, UnmergeableException.class, RidiculousAgeException.class})
    private ResponseEntity<ErrorResponse> handleExceptionToBadRequest(Exception e) {
        System.out.println("########## " + e.getMessage());
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(NotFoundException.class)
    private ResponseEntity<Void> handleExceptionToNotFound(Exception e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(Exception.class)
    private ResponseEntity<ErrorResponse> handleOtherException(Exception e) {
        return ResponseEntity.internalServerError().body(new ErrorResponse("서버 내부 오류가 발생하였습니다."));
    }
}
