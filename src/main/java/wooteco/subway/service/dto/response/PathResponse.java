package wooteco.subway.service.dto.response;

import java.util.List;
import java.util.stream.Collectors;
import wooteco.subway.domain.Station;

public class PathResponse {

    private List<StationResponse> stations;
    private int distance;
    private int fare;

    private PathResponse() {
    }

    public PathResponse(final List<Station> stations, final int distance, final int fare) {
        this.stations = stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
        this.distance = distance;
        this.fare = fare;
    }

    public static PathResponse from(final List<Station> stations, final double distance, final double fare) {
        return new PathResponse(stations, (int) distance, (int) fare);
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
