package wooteco.subway.admin.dto;

import java.util.ArrayList;
import java.util.List;

import wooteco.subway.admin.domain.entity.Station;
import wooteco.subway.admin.domain.graph.SubwayShortestPath;

public class PathResponse {
	private int distance;
	private int duration;
	private List<Station> stations;

	public PathResponse() {}

	public PathResponse(int distance, int duration, List<Station> stations) {
		this.distance = distance;
		this.duration = duration;
		this.stations = new ArrayList<>(stations);
	}

	public static PathResponse of(int totalDistance, int totalDuration, List<Station> shortestPath) {
		return new PathResponse(totalDistance, totalDuration, shortestPath);
	}

	public static PathResponse from(SubwayShortestPath subwayShortestPath) {
		int totalDuration = subwayShortestPath.calculateTotalDuration();
		int totalDistance = subwayShortestPath.calculateTotalDistance();
		List<Station> shortestPath = subwayShortestPath.getShortestPath();

		return PathResponse.of(totalDistance, totalDuration, shortestPath);
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
