package wooteco.subway.admin.controller.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import wooteco.subway.admin.dto.response.DefaultResponse;
import wooteco.subway.admin.exception.SubwayException;

@RestControllerAdvice
public class ControllerAdvice {
    @ExceptionHandler(SubwayException.class)
    public ResponseEntity<DefaultResponse<Void>> handleSubwayException(SubwayException e) {
        return ResponseEntity.badRequest().body(DefaultResponse.error(e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<DefaultResponse<Void>> handleSystemException(Exception e) {
        return ResponseEntity.status(500).body(DefaultResponse.error("시스템 에러가 발생했습니다."));
    }
}
