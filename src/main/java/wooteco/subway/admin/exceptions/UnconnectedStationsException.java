package wooteco.subway.admin.exceptions;

public class UnconnectedStationsException extends RuntimeException {
	public UnconnectedStationsException() {
		super("출발역과 도착역 간에 경로를 찾을 수 없습니다.");
	}
}
