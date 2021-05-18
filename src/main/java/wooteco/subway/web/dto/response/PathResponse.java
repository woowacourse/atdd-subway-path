package wooteco.subway.web.dto.response;

import java.util.List;
import java.util.stream.Collectors;
import wooteco.subway.domain.Station;

public class PathResponse {

    private List<StationResponse> stations;
    private int distance;

    public PathResponse() {
    }

    public PathResponse(List<StationResponse> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public static PathResponse of(List<Station> stations, int distance) {
        return new PathResponse(
            stations.stream()
            .map(StationResponse::from)
            .collect(Collectors.toList()),
            distance
        );
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
