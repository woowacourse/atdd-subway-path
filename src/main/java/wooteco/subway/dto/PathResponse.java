package wooteco.subway.dto;

import wooteco.subway.domain.Path;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.fare.Fare;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {
    private List<StationResponse> stations;
    private int distance;
    private int fare;

    public PathResponse(List<StationResponse> stations, int distance, int fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public static PathResponse of(Path path, Fare fare) {
        List<Station> stations = path.getStations();
        List<StationResponse> stationResponses = stations.stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());
        return new PathResponse(stationResponses, path.getDistance(), (int) fare.getAmount());
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
