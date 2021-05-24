package wooteco.subway.path.dto;

import java.util.List;
import wooteco.subway.path.domain.Path;
import wooteco.subway.station.dto.StationResponse;

public class PathResponse {

    private List<StationResponse> stations;
    private int distance;

    public PathResponse() {
    }

    private PathResponse(List<StationResponse> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public static PathResponse of(Path path) {
        return new PathResponse(
                StationResponse.listOf(path.getStations()),
                path.getDistance()
        );
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
