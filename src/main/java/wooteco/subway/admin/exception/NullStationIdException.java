package wooteco.subway.admin.exception;

public class NullStationIdException extends IllegalArgumentException {
	public NullStationIdException() {
		super("출발역이나 도착역은 null값이 될 수 없습니다.");
	}
}
