package wooteco.subway.dto.response;

import java.util.List;
import java.util.stream.Collectors;

import wooteco.subway.domain.path.Path;
import wooteco.subway.domain.property.Distance;
import wooteco.subway.domain.property.Fare;
import wooteco.subway.domain.station.Station;

public class PathResponse {

    private final List<StationResponse> stations;
    private final Integer distance;
    private final Integer fare;

    public PathResponse(List<StationResponse> stations, Integer distance, Integer fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public static PathResponse of(Path path, Fare fare) {
        final List<StationResponse> stations = path.getRoute().stream()
            .map(StationResponse::from)
            .collect(Collectors.toList());
        final int distanceValue = path.getDistance().getValue();
        final int fareAmount = fare.getAmount();
        return new PathResponse(stations, distanceValue, fareAmount);
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public Integer getDistance() {
        return distance;
    }

    public Integer getFare() {
        return fare;
    }
}
