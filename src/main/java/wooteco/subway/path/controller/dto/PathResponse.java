package wooteco.subway.path.controller.dto;

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

    public static PathResponse of(List<Station> stations, int distance) {
        final List<StationResponse> stationResponses = stations.stream()
                .map(it -> StationResponse.of(it))
                .collect(Collectors.toList());
        return new PathResponse(stationResponses, distance);
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
