package wooteco.subway.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import wooteco.subway.admin.dto.ErrorResponse;

/**
 *    예외처리를 하는 클래스입니다.
 *
 *    @author HyungJu An
 */
@ControllerAdvice
public class ExceptionAdvice {
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> handleException(IllegalArgumentException e) {
		return ResponseEntity.badRequest().body(ErrorResponse.of(e.getMessage()));
	}
}
