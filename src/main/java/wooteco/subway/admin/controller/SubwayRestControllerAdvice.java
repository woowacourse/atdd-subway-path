package wooteco.subway.admin.controller;

import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.subway.admin.domain.InvalidSubwayPathException;

@RestControllerAdvice(annotations = RestController.class)
public class SubwayRestControllerAdvice {

	@ExceptionHandler({IllegalArgumentException.class, NullPointerException.class,
		NoSuchElementException.class, InvalidSubwayPathException.class})
	public ResponseEntity<String> handleException(Exception e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
	}
}
