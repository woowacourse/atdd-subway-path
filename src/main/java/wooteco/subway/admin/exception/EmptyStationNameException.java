package wooteco.subway.admin.exception;

public class EmptyStationNameException extends IllegalArgumentException {
	public EmptyStationNameException() {
		super("출발역과 도착역 모두를 입력해주세요.");
	}
}
