package wooteco.subway.admin.exception;

public class NoPathExistsException extends IllegalArgumentException {
	public NoPathExistsException() {
		super("해당 경로는 존재하지 않아요.");
	}
}
