package wooteco.subway.controller.path;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import wooteco.subway.exception.InvalidPathException;

/*
 * 1. 경로 조회 시 출발역 혹은 도착역이 Null이거나 빈 문자열일 경우
 *   - @Valid를 통해 ConstraintViolationException 발생
 *   - getRequestException 메서드를 통해 처리
 *
 * 2. 출발역과 도착역이 같을 경우
 *   - throw를 통해 InvalidPathException 발생
 *   - getRequestException 메서드를 통해 처리
 *
 * 3. 연결이 안 된 경로를 조회하는 경우
 *   - throw를 통해 InvalidPathException 발생
 *   - getRequestException 메서드를 통해 처리
 *
 * */
@RestControllerAdvice()
public class PathControllerAdvice {
	@ExceptionHandler({ConstraintViolationException.class, InvalidPathException.class})
	public ResponseEntity<String> getRequestException(RuntimeException e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
	}
}
