package wooteco.subway.admin.exception;

public class InaccessibleStationException extends RuntimeException{
	public InaccessibleStationException(String message) {
		super(message + "는 갈 수 없는 역 입니다.");
	}
}
