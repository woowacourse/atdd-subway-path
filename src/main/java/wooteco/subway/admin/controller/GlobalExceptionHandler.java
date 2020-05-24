package wooteco.subway.admin.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import wooteco.subway.admin.dto.ErrorResponse;
import wooteco.subway.admin.exception.DisconnectedPathException;
import wooteco.subway.admin.exception.IllegalPathSearchTypeException;
import wooteco.subway.admin.exception.InvalidRequestDataException;
import wooteco.subway.admin.exception.NotFoundResourceException;
import wooteco.subway.admin.exception.SameSourceAndDestinationException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final String INTERNAL_SERVER_ERROR_MESSAGE = "서버 내부에 오류가 있습니다.";

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleGlobalException() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ErrorResponse.of(INTERNAL_SERVER_ERROR_MESSAGE));
    }

    @ExceptionHandler(value = {InvalidRequestDataException.class, IllegalPathSearchTypeException.class,
        SameSourceAndDestinationException.class, DisconnectedPathException.class})
	public ResponseEntity<ErrorResponse> handleClientBadRequestException(RuntimeException e) {
    	return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body(ErrorResponse.of(e.getMessage()));
	}

    @ExceptionHandler(value = NotFoundResourceException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundResourceException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ErrorResponse.of(e.getMessage()));
    }
}
