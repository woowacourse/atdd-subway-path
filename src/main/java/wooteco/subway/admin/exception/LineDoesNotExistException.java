package wooteco.subway.admin.exception;

public class LineDoesNotExistException extends IllegalArgumentException {
	public LineDoesNotExistException() {
		super("해당 노선은 존재하지 않아요.");
	}
}
