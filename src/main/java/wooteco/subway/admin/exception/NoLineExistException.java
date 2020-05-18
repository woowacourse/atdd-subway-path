package wooteco.subway.admin.exception;

public class NoLineExistException extends IllegalArgumentException {
	public NoLineExistException() {
		super("해당 노선은 존재하지 않아요.");
	}
}
