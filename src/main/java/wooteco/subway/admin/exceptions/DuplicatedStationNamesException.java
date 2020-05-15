package wooteco.subway.admin.exceptions;

public class DuplicatedStationNamesException extends RuntimeException {
	public DuplicatedStationNamesException() {
		super("출발역과 도착역은 같을 수 없습니다.");
	}
}
