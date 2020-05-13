package wooteco.subway.admin.dto;

import java.util.List;

public class ShortestPathResponse {
	private List<StationResponse> stations;
	private int distance;
	private int duration;

	private ShortestPathResponse() {
	}

	public ShortestPathResponse(List<StationResponse> stations, int distance, int duration) {
		this.stations = stations;
		this.distance = distance;
		this.duration = duration;
	}

	public List<StationResponse> getStations() {
		return stations;
	}

	public int getDistance() {
		return distance;
	}

	public int getDuration() {
		return duration;
	}
}
