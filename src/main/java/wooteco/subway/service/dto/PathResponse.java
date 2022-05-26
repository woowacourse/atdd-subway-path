package wooteco.subway.service.dto;

import java.util.List;
import java.util.stream.Collectors;

import wooteco.subway.domain.Station;
import wooteco.subway.domain.path.Path;

public class PathResponse {

    private final List<StationResponse> stations;
    private final int distance;
    private final int fare;

    private PathResponse(List<Station> stations, int distance, int fare) {
        this.stations = toResponses(stations);
        this.distance = distance;
        this.fare = fare;
    }

    private List<StationResponse> toResponses(List<Station> stations) {
        return stations.stream()
            .map(StationResponse::from)
            .collect(Collectors.toList());
    }

    public static PathResponse of(Path path, int fare) {
        return new PathResponse(path.getStations(), path.getDistance(), fare);
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
