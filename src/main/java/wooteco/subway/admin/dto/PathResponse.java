package wooteco.subway.admin.dto;

import wooteco.subway.admin.domain.Station;

import java.util.List;

public class PathResponse {
	private List<Station> stations;
	private int totalDistance;
	private int totalDuration;

	public PathResponse() {
	}

	public PathResponse(List<Station> stations, int totalDistance, int totalDuration) {
		this.stations = stations;
		this.totalDistance = totalDistance;
		this.totalDuration = totalDuration;
	}

	public List<Station> getStations() {
		return stations;
	}

	public int getTotalDistance() {
		return totalDistance;
	}

	public int getTotalDuration() {
		return totalDuration;
	}
}
