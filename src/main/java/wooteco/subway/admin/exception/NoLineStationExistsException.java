package wooteco.subway.admin.exception;

public class NoLineStationExistsException extends IllegalArgumentException {
	public NoLineStationExistsException() {
		super("존재하지 않는 구간이에요.");
	}
}