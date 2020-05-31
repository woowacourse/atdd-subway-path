package wooteco.subway.domain;

import java.util.Objects;

public class LineStation {
	private Long preStationId;
	private Long stationId;
	private int distance;
	private int duration;

	public LineStation(Long preStationId, Long stationId, int distance, int duration) {
		this.preStationId = preStationId;
		this.stationId = stationId;
		this.distance = distance;
		this.duration = duration;
	}

	public void updatePreLineStation(Long preStationId) {
		this.preStationId = preStationId;
	}

	public boolean isFirstStation() {
		return Objects.isNull(preStationId);
	}

	public boolean isPreStationOf(LineStation lineStation) {
		return stationId.equals(lineStation.preStationId);
	}

	public boolean isNotFirstStation() {
		return !isFirstStation();
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
