package wooteco.subway.dto.path;

import java.util.List;
import java.util.stream.Collectors;
import wooteco.subway.domain.PathResult;
import wooteco.subway.dto.station.StationResponse;

public class PathResponse {

    private final List<StationResponse> stations;
    private final int distance;
    private final int fare;

    public PathResponse(PathResult pathResult) {
        this.stations = pathResult.getStations().stream()
                .map(StationResponse::new)
                .collect(Collectors.toList());
        this.distance = pathResult.getDistance();
        this.fare = pathResult.getFare();
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
