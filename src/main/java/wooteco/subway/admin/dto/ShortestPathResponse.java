package wooteco.subway.admin.dto;

import java.util.List;
import java.util.Objects;

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

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		ShortestPathResponse that = (ShortestPathResponse)o;
		return distance == that.distance &&
			duration == that.duration &&
			Objects.equals(stations, that.stations);
	}

	@Override
	public int hashCode() {
		return Objects.hash(stations, distance, duration);
	}

	@Override
	public String toString() {
		return "ShortestPathResponse{" +
			"stations=" + stations +
			", distance=" + distance +
			", duration=" + duration +
			'}';
	}
}
