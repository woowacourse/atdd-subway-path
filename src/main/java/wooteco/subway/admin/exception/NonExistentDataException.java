package wooteco.subway.admin.exception;

public class NonExistentDataException extends RuntimeException {
	public NonExistentDataException(String message) {
		super(message + ": 데이터는 존재 하지 않습니다.");
	}
}
