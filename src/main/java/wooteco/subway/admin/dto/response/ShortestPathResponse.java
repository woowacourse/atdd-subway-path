package wooteco.subway.admin.dto.response;

import wooteco.subway.admin.domain.ShortestPath;
import wooteco.subway.admin.domain.Station;

import java.util.List;

public class ShortestPathResponse {
	private List<Station> path;
	private int distance;
	private int duration;

	public ShortestPathResponse() {
	}

	private ShortestPathResponse(List<Station> path, int distance, int duration) {
		this.path = path;
		this.distance = distance;
		this.duration = duration;
	}

	public static ShortestPathResponse of(ShortestPath shortestPath) {
		return new ShortestPathResponse(shortestPath.getShortestPath(), shortestPath.calculateShortestDistance(),
				shortestPath.calculateShortestDuration());
	}

	public List<Station> getPath() {
		return path;
	}

	public int getDistance() {
		return distance;
	}

	public int getDuration() {
		return duration;
	}
}
