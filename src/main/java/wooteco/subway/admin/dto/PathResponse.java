package wooteco.subway.admin.dto;

import java.util.ArrayList;
import java.util.List;

public class PathResponse {
	private int distance;
	private int duration;
	private List<StationResponse> stationResponses;

	public PathResponse() {
	}

	public PathResponse(int distance, int duration, List<StationResponse> stations) {
		this.distance = distance;
		this.duration = duration;
		this.stationResponses = new ArrayList<>(stations);
	}

	public static PathResponse of(int totalDistance, int totalDuration, List<StationResponse> shortestPath) {
		return new PathResponse(totalDistance, totalDuration, shortestPath);
	}

	public int getDistance() {
		return distance;
	}

	public int getDuration() {
		return duration;
	}

	public List<StationResponse> getStationResponses() {
		return stationResponses;
	}
}
