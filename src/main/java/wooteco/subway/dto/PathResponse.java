package wooteco.subway.dto;

import java.util.List;
import java.util.stream.Collectors;
import wooteco.subway.domain.PathSummary;

public class PathResponse {

    private final List<StationResponse> stations;
    private final int distance;
    private final int fare;

    public PathResponse(PathSummary summary) {
        this.stations = summary.getPath().getStations().stream()
            .map(StationResponse::new)
            .collect(Collectors.toList());
        this.distance = summary.getPath().getDistance();
        this.fare = summary.getFare();
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
