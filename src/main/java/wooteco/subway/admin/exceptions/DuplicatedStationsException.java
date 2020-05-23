package wooteco.subway.admin.exceptions;

public class DuplicatedStationsException extends RuntimeException {
	public DuplicatedStationsException() {
		super("출발역과 도착역은 같을 수 없습니다.");
	}
}
