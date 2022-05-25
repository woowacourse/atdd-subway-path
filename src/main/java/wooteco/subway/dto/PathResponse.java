package wooteco.subway.dto;

import wooteco.subway.domain.ShortestPath;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {

    private final List<StationResponse> stations;
    private final int distance;
    private final int fare;

    private PathResponse() {
        this(null, 0, 0);
    }

    public PathResponse(List<StationResponse> stations, int distance, int fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public static PathResponse from(ShortestPath shortestPath, int fare) {
        List<StationResponse> stations = shortestPath.getVertexes()
                .stream().map(StationResponse::from)
                .collect(Collectors.toUnmodifiableList());
        return new PathResponse(stations, shortestPath.getDistance(), fare);
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
