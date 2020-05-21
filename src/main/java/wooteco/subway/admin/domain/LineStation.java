package wooteco.subway.admin.domain;

public class LineStation {
	private final Long preStationId;
	private final Long stationId;
	private final int distance;
	private final int duration;

	LineStation(Long preStationId, Long stationId, int distance, int duration) {
		this.preStationId = preStationId;
		this.stationId = stationId;
		this.distance = distance;
		this.duration = duration;
	}

	public static LineStation of(Long preStationId, Long stationId, int distance, int duration) {
		return new LineStation(preStationId, stationId, distance, duration);
	}

	public LineStation updatePreLineStation(Long preStationId) {
		return new LineStation(preStationId, this.stationId, this.distance, this.duration);
	}

	public Long getPreStationId() {
		return preStationId;
	}

	public Long getStationId() {
		return stationId;
	}

	public int getDistance() {
		return distance;
	}

	public int getDuration() {
		return duration;
	}
}
