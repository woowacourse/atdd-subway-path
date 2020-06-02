package wooteco.subway.admin.path.service.dto;

import java.util.ArrayList;
import java.util.List;

import wooteco.subway.admin.station.service.dto.StationResponse;

public class PathResponse {

	private List<StationResponse> stations;
	private int distance;
	private int duration;

	public PathResponse() {
	}

	public PathResponse(List<StationResponse> stations, int distance, int duration) {
		this.stations = new ArrayList<>(stations);
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
