package wooteco.subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import wooteco.subway.dto.response.ErrorResponse;
import wooteco.subway.exception.SubwayException;

@ControllerAdvice
@Repository
public class ExceptionController {

    @ExceptionHandler(SubwayException.class)
    public ResponseEntity<ErrorResponse> subwayException(SubwayException exception) {
        return ResponseEntity.status(exception.getHttpStatus()).body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> runtimeExceptionHandler(RuntimeException e) {
        return ResponseEntity.badRequest().body(new ErrorResponse("서버 에러가 발생했습니다."));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(Exception e) {
        return ResponseEntity.internalServerError().body(new ErrorResponse("서버 에러가 발생했습니다."));
    }
}
