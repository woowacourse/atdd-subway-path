package wooteco.subway.admin.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.subway.admin.domain.InvalidSubwayPathException;

@RestControllerAdvice
public class InvalidSubwayPathAdvice {

	@ExceptionHandler(InvalidSubwayPathException.class)
	public ResponseEntity<String> handleInvalidSubwayPathException(InvalidSubwayPathException e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
	}
}
