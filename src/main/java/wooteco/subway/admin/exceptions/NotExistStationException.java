package wooteco.subway.admin.exceptions;

public class NotExistStationException extends RuntimeException {
	public NotExistStationException(Long id) {
		super(String.format("%d를(을) id로 가진 역이 존재하지 않습니다.", id));
	}
}
