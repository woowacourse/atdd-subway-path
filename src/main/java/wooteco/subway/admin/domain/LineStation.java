package wooteco.subway.admin.domain;

import java.util.List;

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

	public Station findMatchingStation(List<Station> stations) {
		return stations.stream()
			.filter(station -> station.getId().equals(this.getStationId()))
			.findFirst()
			.orElseThrow(IllegalArgumentException::new);
	}

	public boolean isNotFirstLineStation() {
		return preStationId != null;
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
