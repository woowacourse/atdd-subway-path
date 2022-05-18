package wooteco.subway.dto;

import java.util.List;
import java.util.stream.Collectors;
import wooteco.subway.domain.Path;

public class PathResponse {

    private final List<StationResponse> stations;
    private final int distance;
    private final int fare;

    public PathResponse(List<StationResponse> stations, int distance, int fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public static PathResponse of(Path path) {
        List<StationResponse> stations = path.getStations().stream()
            .map(StationResponse::of)
            .collect(Collectors.toList());
        return new PathResponse(stations, path.getDistance(), path.calculateFare());
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getFare() {
        return fare;
    }
}
