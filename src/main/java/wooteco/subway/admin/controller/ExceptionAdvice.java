package wooteco.subway.admin.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.subway.admin.dto.ExceptionResponse;
import wooteco.subway.admin.exceptions.DuplicatedStationNamesException;
import wooteco.subway.admin.exceptions.NotExistStationException;
import wooteco.subway.admin.exceptions.UnconnectedStationsException;

@RestControllerAdvice
public class ExceptionAdvice {

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(NotExistStationException.class)
	public ExceptionResponse notExistStation(NotExistStationException e) {
		return new ExceptionResponse("NOT_EXIST_STATION", e.getMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(DuplicatedStationNamesException.class)
	public ExceptionResponse duplicatedStation(DuplicatedStationNamesException e) {
		return new ExceptionResponse("DUPLICATED_STATION", e.getMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(UnconnectedStationsException.class)
	public ExceptionResponse unconnectedStation(UnconnectedStationsException e) {
		return new ExceptionResponse("UNCONNECTED_STATIONS", e.getMessage());
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(RuntimeException.class)
	public ExceptionResponse serverError(RuntimeException e) {
		return new ExceptionResponse("SERVER_ERROR", e.getMessage());
	}
}
