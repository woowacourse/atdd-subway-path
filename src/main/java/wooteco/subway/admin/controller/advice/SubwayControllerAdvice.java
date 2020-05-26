package wooteco.subway.admin.controller.advice;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.subway.admin.dto.response.ExceptionResponse;
import wooteco.subway.admin.exception.EmptyStationNameException;
import wooteco.subway.admin.exception.NoResourceExistException;
import wooteco.subway.admin.exception.SourceEqualsTargetException;

@RestControllerAdvice
public class SubwayControllerAdvice {
	private static final Logger LOGGER = LogManager.getLogger("SubwayControllerAdvice");

	@ExceptionHandler({
			SourceEqualsTargetException.class,
			EmptyStationNameException.class
	})
	public ResponseEntity<ExceptionResponse> getBadRequestException(RuntimeException e) {
		LOGGER.error(e);
		return new ResponseEntity<>(ExceptionResponse.of(e.getMessage()), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({
			NoResourceExistException.class
	})
	public ResponseEntity<ExceptionResponse> getResourceNotFoundException(RuntimeException e) {
		LOGGER.error(e);
		return new ResponseEntity<>(ExceptionResponse.of(e.getMessage()), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ExceptionResponse> getUnexpectedException(Exception e) {
		LOGGER.error(e);
		return new ResponseEntity<>(ExceptionResponse.of("서버 오류가 발생했어요."), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
