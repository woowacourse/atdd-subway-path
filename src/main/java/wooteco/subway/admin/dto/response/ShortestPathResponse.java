package wooteco.subway.admin.dto.response;

import wooteco.subway.admin.domain.Station;

import java.util.List;

public class ShortestPathResponse {
	private List<Station> path;
	private int distance;
	private int duration;

	public ShortestPathResponse() {
	}

	public ShortestPathResponse(List<Station> path, int distance, int duration) {
		this.path = path;
		this.distance = distance;
		this.duration = duration;
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
