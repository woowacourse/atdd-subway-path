package wooteco.subway.admin.controller.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.subway.admin.dto.ExceptionResponse;
import wooteco.subway.admin.exception.*;

@RestControllerAdvice
public class SubwayControllerAdvice {
	@ExceptionHandler(SourceEqualsTargetException.class)
	public ResponseEntity<ExceptionResponse> getSourceEqualsTargetException(SourceEqualsTargetException e) {
		return new ResponseEntity<>(ExceptionResponse.of(e.getMessage()), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(NoStationNameExistsException.class)
	public ResponseEntity<ExceptionResponse> getNoStationNameExistsException(NoStationNameExistsException e) {
		return new ResponseEntity<>(ExceptionResponse.of(e.getMessage()), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(EmptyStationNameException.class)
	public ResponseEntity<ExceptionResponse> getEmptyStationNameException(EmptyStationNameException e) {
		return new ResponseEntity<>(ExceptionResponse.of(e.getMessage()), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(NoCriteriaExistsException.class)
	public ResponseEntity<ExceptionResponse> getNoCriteriaExistsException(NoCriteriaExistsException e) {
		return new ResponseEntity<>(ExceptionResponse.of(e.getMessage()), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(LineDoesNotExistException.class)
	public ResponseEntity<ExceptionResponse> getLineDoesNotExistException(LineDoesNotExistException e) {
		return new ResponseEntity<>(ExceptionResponse.of(e.getMessage()), HttpStatus.BAD_REQUEST);
	}
}
