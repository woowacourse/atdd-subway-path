package wooteco.subway.ui.dto;

import java.util.List;
import java.util.stream.Collectors;

import wooteco.subway.domain.vo.Path;
import wooteco.subway.domain.Station;

public class PathResponse {
    private final List<StationResponse> stations;
    private final int distance;
    private final int fare;

    public PathResponse(List<StationResponse> stations, int distance, int fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public static PathResponse of(Path path, int fare) {
        List<Station> stations = path.getStations();
        List<StationResponse> stationResponses = stations.stream()
            .map(StationResponse::from)
            .collect(Collectors.toList());
        return new PathResponse(stationResponses, path.getDistance(), fare);
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
