package wooteco.subway.dto;

import java.util.List;
import java.util.stream.Collectors;
import wooteco.subway.domain.Fare;
import wooteco.subway.domain.Station;

public class PathResponse {
    private final List<StationResponse> stations;
    private final Integer distance;
    private final Long fare;

    public PathResponse(List<StationResponse> stations, Integer distance, Long fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public static PathResponse of(List<Station> stations, int distance, Fare fare) {
        List<StationResponse> stationResponses = stations.stream()
                .map(station -> new StationResponse(station.getId(), station.getName()))
                .collect(Collectors.toList());
        return new PathResponse(stationResponses,distance, fare.getValue());
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public Integer getDistance() {
        return distance;
    }

    public Long getFare() {
        return fare;
    }
}
