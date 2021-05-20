package wooteco.subway.web.dto;

import java.util.stream.Collectors;
import wooteco.subway.domain.Station;
import wooteco.subway.web.dto.StationResponse;

import java.util.List;

public class PathResponse {
    private List<StationResponse> stations;
    private int distance;

    public PathResponse() {
    }

    public PathResponse(List<Station> stations, int distance) {
        this.stations = StationResponse.listOf(stations);
        this.distance = distance;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
