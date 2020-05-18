package wooteco.subway.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {
	@ExceptionHandler(value = RuntimeException.class)
	public ResponseEntity<String> getException(RuntimeException e) {
		return ResponseEntity.badRequest().body(e.getMessage());
	}

	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<String> getException(Exception e) {
		return ResponseEntity.badRequest().body(e.getMessage());
	}
}
