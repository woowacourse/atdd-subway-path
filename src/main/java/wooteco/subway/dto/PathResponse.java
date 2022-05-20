package wooteco.subway.dto;

import java.util.List;
import java.util.stream.Collectors;
import wooteco.subway.domain.Path;

public class PathResponse {

    private List<StationResponse> stations;
    private int distance;
    private int fare;

    public PathResponse() {
    }

    public PathResponse(Path path, int fare) {
        this.stations = toResponseStations(path);
        this.distance = path.getDistance();
        this.fare = fare;
    }

    private List<StationResponse> toResponseStations(Path path) {
        return path.getStations().stream()
                .map(StationResponse::new)
                .collect(Collectors.toList());
    }

    public PathResponse(List<StationResponse> stations, int distance, int fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
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
