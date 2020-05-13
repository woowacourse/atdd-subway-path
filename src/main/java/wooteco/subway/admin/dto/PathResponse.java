package wooteco.subway.admin.dto;

import java.util.List;
import java.util.stream.Collectors;

import wooteco.subway.admin.domain.Station;

public class PathResponse {
	private List<StationResponse> stationResponses;
	private int totalDistance;
	private int totalDuration;

	private PathResponse() {
	}

	public PathResponse(final List<StationResponse> stationResponses, final int totalDistance,
		final int totalDuration) {
		this.stationResponses = stationResponses;
		this.totalDistance = totalDistance;
		this.totalDuration = totalDuration;
	}

	public static PathResponse of(final List<Station> stations, final int totalDistance,
		final int totalDuration) {
		return stations.stream()
			.map(StationResponse::of)
			.collect(Collectors.collectingAndThen(Collectors.toList(),
				list -> new PathResponse(list, totalDistance, totalDuration)));
	}

	public List<StationResponse> getStationResponses() {
		return stationResponses;
	}

	public int getTotalDistance() {
		return totalDistance;
	}

	public int getTotalDuration() {
		return totalDuration;
	}
}
