package wooteco.subway.admin.controller.advice;

import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import wooteco.subway.admin.dto.response.StandardResponse;
import wooteco.subway.admin.exception.SubwayException;

@RestControllerAdvice
public class ControllerAdvice {
    @ExceptionHandler(SubwayException.class)
    public ResponseEntity<StandardResponse<Void>> handleSubwayException(SubwayException e) {
        return ResponseEntity.badRequest().body(StandardResponse.error(e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardResponse<Void>> handleSystemException(Exception e) {
        return ResponseEntity.status(500).body(StandardResponse.error("시스템 에러가 발생했습니다."));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardResponse<Void>> handleValidationException(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult().getAllErrors()
            .stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .collect(Collectors.joining(""));
        return ResponseEntity.badRequest().body(StandardResponse.error(errorMessage));
    }
}
