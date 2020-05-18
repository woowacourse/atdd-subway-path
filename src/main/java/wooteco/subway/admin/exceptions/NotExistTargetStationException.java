package wooteco.subway.admin.exceptions;

public class NotExistTargetStationException extends RuntimeException {
	public NotExistTargetStationException() {
		super("존재하지 않는 도착역 입니다.");
	}
}
