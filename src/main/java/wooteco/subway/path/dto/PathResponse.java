package wooteco.subway.path.dto;

import java.util.List;
import java.util.stream.Collectors;

import wooteco.subway.station.dto.StationResponse;

import wooteco.subway.station.dto.StationResponse;

public class PathResponse {
    private List<StationResponse> stations;
    private int distance;

    public PathResponse() {
    }

    public PathResponse(Path path) {
        this.stations = path.getStations().stream()
            .map(station -> new StationResponse(station.getId(), station.getName()))
            .collect(Collectors.toList());
        this.distance = path.getDistance();
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
