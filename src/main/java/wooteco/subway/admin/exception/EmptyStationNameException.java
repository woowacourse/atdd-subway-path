package wooteco.subway.admin.exception;

public class EmptyStationNameException extends FindingShortestPathException {
	public EmptyStationNameException() {
		super("출발역과 도착역 모두를 입력해주세요.");
	}
}
