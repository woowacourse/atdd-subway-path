package wooteco.subway.admin.exceptions;

public class NotExistStationException extends RuntimeException {
	public NotExistStationException() {
		super("저장되지 않은 역을 입력하셨습니다.");
	}
}
