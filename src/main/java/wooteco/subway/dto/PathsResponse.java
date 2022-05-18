package wooteco.subway.dto;

import java.util.List;
import java.util.stream.Collectors;
import wooteco.subway.domain.Paths;
import wooteco.subway.domain.Station;

public class PathsResponse {

    private final List<StationResponse> stations;
    private final double distance;
    private final int fare;

    public PathsResponse(final List<StationResponse> stations, final double distance, final int fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public static PathsResponse of(final Paths paths) {
        List<StationResponse> stationResponses = convertToStationResponses(paths.getStations());
        return new PathsResponse(stationResponses, paths.getDistance(), paths.getFare());
    }

    private static List<StationResponse> convertToStationResponses(final List<Station> stations) {
        return stations.stream()
                .map(station -> new StationResponse(station.getId(), station.getName()))
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
