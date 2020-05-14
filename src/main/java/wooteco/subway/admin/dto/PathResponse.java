package wooteco.subway.admin.dto;

import java.util.ArrayList;
import java.util.List;

import wooteco.subway.admin.domain.entity.Station;

public class PathResponse {
	private int distance;
	private int duration;
	private List<Station> stations;

	public PathResponse() {
	}

	public PathResponse(int distance, int duration, List<Station> stations) {
		this.distance = distance;
		this.duration = duration;
		this.stations = new ArrayList<>(stations);
	}

	public static PathResponse of(int totalDistance, int totalDuration, List<Station> shortestPath) {
		return new PathResponse(totalDistance, totalDuration, shortestPath);
	}

	public int getDistance() {
		return distance;
	}

	public int getDuration() {
		return duration;
	}

	public List<Station> getStations() {
		return stations;
	}
}
