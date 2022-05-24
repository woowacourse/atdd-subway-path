package wooteco.subway.controller;

import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import wooteco.subway.controller.dto.ExceptionResponse;

@RestControllerAdvice
public class ControllerAdvice {

	@ExceptionHandler
	public ResponseEntity<ExceptionResponse> handleIllegalInput(IllegalArgumentException exception) {
		return ResponseEntity.badRequest()
			.body(new ExceptionResponse(exception.getMessage()));
	}

	@ExceptionHandler
	public ResponseEntity<ExceptionResponse> handleNoData(NoSuchElementException exception) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
			.body(new ExceptionResponse(exception.getMessage()));
	}
}
