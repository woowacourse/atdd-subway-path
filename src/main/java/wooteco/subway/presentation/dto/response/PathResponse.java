package wooteco.subway.presentation.dto.response;

import wooteco.subway.domain.path.Path;

import java.util.List;

public class PathResponse {
    private final List<StationResponse> stations;
    private final int distance;

    public PathResponse(Path path) {
        this(
                StationResponse.listOf(path.getPathStations()),
                path.getDistance());
    }

    public PathResponse(List<StationResponse> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
