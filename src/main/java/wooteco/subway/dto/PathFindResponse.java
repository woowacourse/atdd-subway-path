package wooteco.subway.dto;

import wooteco.subway.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

public class PathFindResponse {

    private final List<StationResponse> stations;
    private final int distance;
    private final int fare;

    public PathFindResponse(List<StationResponse> stations, int distance, int fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public static PathFindResponse of(List<Station> stations, int distance, int fare) {
        List<StationResponse> stationResponses = stations.stream()
                .map(s -> new StationResponse(s.getId(), s.getName()))
                .collect(Collectors.toList());
        return new PathFindResponse(stationResponses, distance, fare);
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
