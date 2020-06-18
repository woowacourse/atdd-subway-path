package wooteco.subway.admin.controller.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import wooteco.subway.admin.dto.ExceptionResponse;
import wooteco.subway.admin.exception.FindingShortestPathException;

@RestControllerAdvice
public class SubwayControllerAdvice {
	@ExceptionHandler({FindingShortestPathException.class})
	public ResponseEntity<ExceptionResponse> getFindingShortestPathException(FindingShortestPathException e) {
		return new ResponseEntity<>(ExceptionResponse.of(e.getMessage()), HttpStatus.BAD_REQUEST);
	}
}
