package wooteco.subway.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import wooteco.subway.admin.exception.InaccessibleStationException;
import wooteco.subway.admin.exception.NonExistentDataException;

@ControllerAdvice
public class ExceptionController {
	@ExceptionHandler(InaccessibleStationException.class)
	public ResponseEntity<String> handleInaccessibleStationException() {
		return ResponseEntity.badRequest().body("갈 수 없는 역입니다. 다시 입력해주세요.");
	}

	@ExceptionHandler(NonExistentDataException.class)
	public ResponseEntity<String> handleNonExistentDataException() {
		return ResponseEntity.badRequest().body("존재하지 않는 데이터입니다.");
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleException() {
		return ResponseEntity.badRequest().body("시스템 에러");
	}
}