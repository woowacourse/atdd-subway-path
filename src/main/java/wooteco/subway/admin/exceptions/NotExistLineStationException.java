package wooteco.subway.admin.exceptions;

public class NotExistLineStationException extends RuntimeException {
	public NotExistLineStationException(Long preStationId, Long stationId) {
		super(String.format("Pre-Station id가 %d, Station id가 %d인 구간(LineStation)이 존재하지 않습니다.",
		                    preStationId, stationId));
	}
}
