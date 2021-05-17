package wooteco.subway.path.controller.dto;

import wooteco.subway.path.domain.Path;
import wooteco.subway.station.controller.dto.StationResponse;
import wooteco.subway.station.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {
    private List<StationResponse> stations;
    private int distance;

    public PathResponse() {
    }

    public PathResponse(List<StationResponse> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public static PathResponse of(Path shortestPath) {
        final List<Station> path = shortestPath.getPath();
        final int distance = shortestPath.getDistance();
        return new PathResponse(makeStationResponse(path), distance);
    }

    private static List<StationResponse> makeStationResponse(List<Station> path) {
        return path.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
