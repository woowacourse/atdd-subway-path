package wooteco.subway.admin.exception;

public class SameSourceAndDestinationException extends IllegalArgumentException {
	public SameSourceAndDestinationException() {
		super("출발역과 도착역이 같을 수 없습니다. 역을 다시 지정해주세요.");
	}
}
