package wooteco.subway.admin.exceptions;

public class NotExistSourceStationException extends RuntimeException {
	public NotExistSourceStationException() {
		super("존재하지 않는 출발역 입니다.");
	}
}
