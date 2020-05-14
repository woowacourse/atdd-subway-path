package wooteco.subway.admin.dto;

import java.util.List;
import java.util.stream.Collectors;

import wooteco.subway.admin.domain.Station;

public class PathResponse {
	private List<StationResponse> stations;
	private int distance;
	private int duration;

	private PathResponse() {
	}

	public PathResponse(final List<StationResponse> stations, final int distance,
		final int duration) {
		this.stations = stations;
		this.distance = distance;
		this.duration = duration;
	}

	public static PathResponse of(final List<Station> stations, final int distance,
		final int duration) {
		return stations.stream()
			.map(StationResponse::of)
			.collect(Collectors.collectingAndThen(Collectors.toList(),
				list -> new PathResponse(list, distance, duration)));
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
