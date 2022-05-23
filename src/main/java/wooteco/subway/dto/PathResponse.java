package wooteco.subway.dto;

import java.util.List;
import java.util.stream.Collectors;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Station;

public class PathResponse {

    private final List<StationResponse> stations;
    private final double distance;
    private final int fare;


    public PathResponse(List<StationResponse> stations, double distance, int fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public static PathResponse from(final Path path) {
        return new PathResponse(convertToStationResponses(path.getStations()), path.getDistance(), path.getFare());
    }

    private static List<StationResponse> convertToStationResponses(final List<Station> stations) {
        return stations.stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public double getDistance() {
        return distance;
    }

    public int getFare() {
        return fare;
    }
}
