package wooteco.subway.admin.exception;

public class NonExistentDataException extends RuntimeException {
	public NonExistentDataException(String message) {
		super(message);
	}
}
