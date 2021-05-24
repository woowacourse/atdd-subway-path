package wooteco.subway;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import wooteco.subway.exception.SubwayException;
import wooteco.subway.exception.dto.ExceptionResponse;

@ControllerAdvice
public class SubwayControllerAdvice {
    @ExceptionHandler(SubwayException.class)
    public ResponseEntity<ExceptionResponse> authorization(SubwayException e) {
        return ResponseEntity
                .status(e.httpStatus())
                .body(e.body());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BindingResult> methodArgumentNotValid(MethodArgumentNotValidException e) {
        return ResponseEntity
                .badRequest()
                .body(e.getBindingResult());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> exception(Exception e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ExceptionResponse("서버에서 오류가 발생했습니다."));
    }
}
