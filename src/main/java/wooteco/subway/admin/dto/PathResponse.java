package wooteco.subway.admin.dto;

import java.util.List;

public class PathResponse {
	private List<String> stationNames;
	private int totalDistance;
	private int totalDuration;

	public PathResponse() {
	}

	public PathResponse(List<String> stationNames, int totalDistance, int totalDuration) {
		this.stationNames = stationNames;
		this.totalDistance = totalDistance;
		this.totalDuration = totalDuration;
	}

	public List<String> getStationNames() {
		return stationNames;
	}

	public int getTotalDistance() {
		return totalDistance;
	}

	public int getTotalDuration() {
		return totalDuration;
	}
}
