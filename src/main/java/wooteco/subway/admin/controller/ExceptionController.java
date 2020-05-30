package wooteco.subway.admin.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import wooteco.subway.admin.exception.DisconnectedPathException;
import wooteco.subway.admin.exception.NoSuchStationException;
import wooteco.subway.admin.exception.NullStationIdException;
import wooteco.subway.admin.exception.SameSourceAndDestinationException;

@RestControllerAdvice
public class ExceptionController {
	@ExceptionHandler(value = {DisconnectedPathException.class, NoSuchStationException.class,
		NullStationIdException.class, SameSourceAndDestinationException.class})
	public ResponseEntity<String> getException(RuntimeException e) {
		return ResponseEntity.badRequest().body(e.getMessage());
	}

	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<String> getException(Exception e) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
	}
}
