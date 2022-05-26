package wooteco.subway.dto;

import wooteco.subway.domain.Path;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {

    private List<StationResponse> stations;
    private int distance;
    private double fare;

    private PathResponse() {
    }

    public static PathResponse from(final Path path) {
        List<StationResponse> stationResponses = path.getStations().stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());
        return new PathResponse(stationResponses, path.getDistance(), path.getFare());
    }

    public PathResponse(final List<StationResponse> stations, final int distance, final double fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public double getFare() {
        return fare;
    }
}
