package wooteco.subway.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import wooteco.subway.admin.dto.ErrorResponse;
import wooteco.subway.admin.exception.InaccessibleStationException;
import wooteco.subway.admin.exception.NonExistentDataException;

@ControllerAdvice
public class ExceptionController {
	@ExceptionHandler(InaccessibleStationException.class)
	public ResponseEntity<ErrorResponse> handleInaccessibleStationException() {
		ErrorResponse errorResponse = new ErrorResponse("INACCESSIBLE_STATION");
		return ResponseEntity.badRequest().body(errorResponse);
	}

	@ExceptionHandler(NonExistentDataException.class)
	public ResponseEntity<ErrorResponse> handleNonExistentDataException() {
		ErrorResponse errorResponse = new ErrorResponse("NON_EXISTENT_DATA");
		return ResponseEntity.badRequest().body(errorResponse);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleException() {
		ErrorResponse errorResponse = new ErrorResponse("SYSTEM_ERROR");
		return ResponseEntity.badRequest().body(errorResponse);
	}
}